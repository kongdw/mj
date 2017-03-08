package org.liprudent.majiang.engine.round.impl

import org.liprudent.majiang.engine.game.IGame
import java.io.IOException
import java.io.ObjectInputStream
import org.liprudent.majiang.engine.round.Scoring
import java.util.Observable
import org.liprudent.majiang.engine.event.KindOfWall
import org.liprudent.majiang.engine.player.IPlayer
import org.liprudent.majiang.engine.round.{ IRound, Action }
import org.liprudent.majiang.engine.round.State._
import org.liprudent.majiang.engine.round.impl.treatment.{ TreatmentFactory, ITreatment }
import org.liprudent.majiang.engine.tile.{ ITileSets, ITile }
import org.liprudent.majiang.engine.tile.impl.TileSets
import org.slf4j.LoggerFactory
import scala.collection.JavaConversions._
import org.liprudent.majiang.engine.event.KindOfWall.WALL

trait RoundInit {

  private val log = LoggerFactory.getLogger("RoundInit")

  /**
   * Contains all the tiles of the game
   * NOTE: must be overriden in mixed class
   */
  val tileSet: ITileSets

  /**
   * A turn consists in a player having a new tile.
   * Then he can discard, kong or mahjong. 
   * In the former case, the otherPlayers have to choose an action among kong, pung, chow, pass, or mahjong
   */
  var currentTurn: Turn = null

  /**
   * A round consists in several turns.
   * When a turn ends, a new one is appended to this list.
   */
  var turns: List[ITurn] = Nil

  /**
   * The list of all players
   * NOTE: must be overriden in mixed class
   */
  val allPlayers: List[IPlayer]

  /**
   * The player who'll start the round (ie: the one who first get an extra tile from the wall)
   */
  val eastPlayer: IPlayer

  /**
   * Set the currentTurn and add it to the list of turns
   */
  protected def createNewTurn(currentPlayer: IPlayer, extraTile: ITile, source: KindOfWall) = {
    currentTurn = new Turn(currentPlayer, extraTile, source)
    turns = currentTurn :: turns
  }

  /**
   * Give initial tiles in player's hand
   */
  private def givePlayersFirstHand = {
    allPlayers.foreach(p => addTilesToPlayer(p, 13))
  }

  /**
   * Extract tiles from the wall and transfers it to player's concealed hand
   */
  private def addTilesToPlayer(player: IPlayer, nbTiles: Int) = {
    val tiles = tileSet.giveTilesFromWalls(nbTiles)
    log.debug("giving " + tiles + " to " + player)
    player.giveTilesInConcealedHand(tiles, KindOfWall.WALL)
  }

  /**
   * Must be called by constructor's mixins
   */
  protected def initRound = {
    // TODO do the round have to deal with this? Must be an assertion ...
    allPlayers.foreach(p => p.clearAllTiles)

    givePlayersFirstHand

    val extraTile = tileSet.giveTilesFromWalls(1).iterator.next
    createNewTurn(eastPlayer, extraTile, WALL)

  }
}

@serializable class Round(val starter: IPlayer,val gameObserver:java.util.Observer) extends Observable with IRound with RoundInit {

  def this(starter: IPlayer) = this(starter,null)
  private val log = LoggerFactory.getLogger("Round")

  initRound

  /*
   * RoundInit overrides
   */

  override lazy val tileSet: ITileSets = new TileSets

  override lazy val eastPlayer = starter

  override lazy val allPlayers: List[IPlayer] = List(starter, starter.getNext, starter.getNext.getNext, starter.getNext.getNext.getNext)

  /*
	 * IRound overrides
	 */

  override def getCurrentPlayer: IPlayer = { currentTurn.currentPlayer }

  override def getOtherPlayers: java.util.List[IPlayer] = {
    currentTurn.actionPlayers
  }

  override def getTileSet: ITileSets = { tileSet }

  override def handlePlayer(player: IPlayer, action: Action, tile: ITile) = {
    val treatment = TreatmentFactory.getInstance(player, action, this, tile)
    processTreatment(treatment)
  }

  override def handlePlayer(player: IPlayer, action: Action, tiles: java.util.Collection[ITile]) = {
    val treatment = TreatmentFactory.getInstance(player, action, this, tiles)
    processTreatment(treatment)
  }

  override def handlePlayer(player: IPlayer, action: Action) = {
    val treatment = TreatmentFactory.getInstance(player, action, this)
    processTreatment(treatment)
  }

  override def getWinner: IPlayer = {
    assert(isEnd)
    if(currentTurn.endRoundBecauseMahjong) currentTurn.getTurnWinner
    else null
  }

  override def getVisibleTiles: java.util.List[ITile] = {
    val visibles = new scala.collection.mutable.ArrayBuffer[ITile]()

    //add discarded
    val d: scala.collection.mutable.Buffer[ITile] = tileSet.getDiscardedTiles
    visibles ++= d

    //add all opened
    allPlayers.foreach(p => {
      val os: scala.collection.mutable.Set[java.util.SortedSet[ITile]] = p.getTiles.getOpenedHand
      os.foreach({ o =>
        val d: scala.collection.mutable.Set[ITile] = o
        visibles ++= d
      })
    })

    visibles;
  }

  override def doEnd: Boolean = {
    val ret = isEnd
    log.debug("doEnd - isEnd = " +ret);
    if (ret) {
      changePlayerStateWhenRoundEnd
      log.debug("doEnd - notify the game that round ends");
      setChanged
      notifyObservers(null)
      
    }
    return ret
  }
  
  override def getScoring:Scoring = currentTurn.getScoring

  /**
   * Execute the treatment and modify some states if needed
   */
  private def processTreatment(treatment: ITreatment): Boolean = {
    	
	
    //this following statement may change the return value of end()
    val valid = currentTurn.processTreatment(treatment)

    // the turn is over BUT the round is not
    if (!doEnd && currentTurn.endTurn) {
      initNewTurn
      log.debug("new turn has been instantiated")
    }
    valid

  }

  /**
   * Change current player and player's states then instan	ciate a new turn
   */
  private def initNewTurn = {
    // set the next current player and give a tile if needed
    val currentPlayer = getNewCurrentPlayer

    val (tile, source) = if (currentTurn.isKongTurnWinner) {
      //since it's a kong, he get one tile from the dead wall
      Tuple2(getTileSet.giveTileFromDeadWall, KindOfWall.DEAD_WALL)
    } else if (currentTurn.getTurnWinner != null) {
      //someone ate the discarded tile, so don't give any extra tile
      (null, null)
    } else {
      // give a tile from wall to current player
      Tuple2(getTileSet.giveTilesFromWalls(1).iterator.next, KindOfWall.WALL);
    }

    // create a new turn
    createNewTurn(currentPlayer, tile, source)
  }

  private def getNewCurrentPlayer: IPlayer = {
    val eater = currentTurn.getTurnWinner
    if (eater != null) {
      eater
    } else {
      currentTurn.currentPlayer.getNext
    }
  }

  /**
   * @return true if no tiles left or if someone won
   */
  def isEnd: Boolean = {
    val emptyWall = tileSet.getWall.isEmpty
    if (emptyWall) log.debug("Wall is empty")
    else log.debug("Wall is not empty")
    return currentTurn.endRoundBecauseMahjong || (emptyWall && currentTurn.endTurn)
  }

  /**
   * This method will set ROUND_LOOSER/ROUND_WINNER to players
   */
  protected def changePlayerStateWhenRoundEnd = {
    log.debug("changePlayerStateWhenRoundEnd")
    assert(isEnd == true)
    val winner = getWinner
    allPlayers.foreach(p => {
      if (p.equals(winner)) {
        p.setState(ROUND_WINNER)
      } else {
        p.setState(ROUND_LOOSER)
      }
    })
  }
  
  @throws(classOf[IOException])
  private def readObject(in: ObjectInputStream): Unit = {
	  in.defaultReadObject
	  //add the observer
	  //if(gameObserver == null) throw new IllegalStateException("The game observer can not be null");
	  //addObserver(gameObserver);
  }

}
