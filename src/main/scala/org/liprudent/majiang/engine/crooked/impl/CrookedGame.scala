package org.liprudent.majiang.engine.game.impl
import org.liprudent.majiang.engine.player._
import org.liprudent.majiang.engine.tile._
import org.liprudent.majiang.engine.tile.impl._
import org.liprudent.majiang.engine.round._
import org.liprudent.majiang.engine.round.impl._
import org.liprudent.majiang.engine.player.IPlayer.Wind._
import org.liprudent.majiang.engine.player.IPlayer.Wind
import org.slf4j.LoggerFactory
import scala.collection.JavaConversions._

@serializable class CrookedGame(players: java.util.Set[IPlayer],protected val crookedWall: List[ITile]) extends Game(players: java.util.Set[IPlayer]) {

  type Name = String
  lazy val crookedWind: Map[Name,Wind] = Map() ++ players.map(p => (p.getName,p.getWind))
  
  //override to be visible in test
  override def findPlayer(name: String): IPlayer = super.findPlayer(name)

  override def chooseWindAndLink: IPlayer = {
    var eastP: IPlayer = null
    var westP: IPlayer = null
    var southP: IPlayer = null
    var northP: IPlayer = null

    players.foreach {p=>
        p.getWind match {
          case EAST => eastP = p
          case WEST => westP = p
          case NORTH => northP = p
          case SOUTH => southP = p
        }
        
        //in order to generate a wind event
        p.setWind(p.getWind)
    }

    eastP.setNext(northP)
    northP.setPrevious(eastP)
    eastP.setPrevious(southP)
    southP.setNext(eastP)
    northP.setNext(westP)
    westP.setPrevious(northP)
    westP.setNext(southP)
    southP.setPrevious(westP)
    eastP
  }

  override def instanciateRound(firstPlayer: IPlayer): IRound = {
    assert(crookedWall.length == 136)
    val round = new CrookedRound(crookedWall, firstPlayer)
    round.addObserver(this)
    round
    
  }
  
  
  
}

class CrookedRound(crookedWall: List[ITile], currentPlayer: IPlayer) extends Round(currentPlayer: IPlayer){
  lazy val tileSets = new CrookedTileSets(crookedWall)
}

class CrookedTileSets(crookedTiles:List[ITile]) extends TileSets {
    private val log = LoggerFactory.getLogger("CrookedTileSets")
	log.debug("crookedTiles = " + crookedTiles.toString)
	wall = new java.util.ArrayList[ITile]()
	deadWall = new java.util.ArrayList[ITile]()
	crookedTiles.slice(0, TileSets.WALL_SIZE) foreach (tile => wall.add(tile))
	crookedTiles.slice(TileSets.WALL_SIZE, crookedTiles.length).foreach(tile => deadWall.add(tile))
	log.debug("wall = " + wall.toString)
}
