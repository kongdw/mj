package org.liprudent.majiang.testhelper

import org.liprudent.majiang.engine.game.impl.CrookedRound
import org.liprudent.majiang.engine.round.IRound
import org.liprudent.majiang.engine.game.IGame
import org.liprudent.majiang.engine.game.impl.CrookedGame
import org.liprudent.majiang.engine.tile.impl.TileSets
import scala.collection.mutable.HashMap
import scala.collection.mutable.ArrayBuffer
import org.liprudent.majiang.engine.player.IPlayer.Wind._
import org.liprudent.majiang.engine.player.impl.Player
import org.liprudent.majiang.engine.player.IPlayer
import org.liprudent.majiang.engine.tile.ITile;
import scala.collection.mutable.Buffer
import scala.collection.JavaConversions._

object TestHelper {

  def makeChainedPlayers: IPlayer = {

    val playerE = Player.instanceOf("P1", EAST, null, null)
    val playerN = Player.instanceOf("P2", NORTH, null, playerE)
    val playerW = Player.instanceOf("P3", WEST, null, playerN)
    val playerS = Player.instanceOf("P2", SOUTH, null, playerW)

    playerE.setNext(playerN);
    playerE.setPrevious(playerS);
    playerN.setNext(playerW);
    playerW.setNext(playerS);
    playerS.setNext(playerE);
    playerE
  }

  /**
   * @param p1ConcealedTiles
   * @param p2ConcealedTiles
   * @param p3ConcealedTiles
   * @param p4ConcealedTiles
   * @param wall
   * @return A List ot ITile representing the crooked wall corresponding to input parameters
   */
  def makeCrookedWall(p1ConcealedTiles: List[ITile], p2ConcealedTiles: List[ITile], p3ConcealedTiles: List[ITile], p4ConcealedTiles: List[ITile], wall: List[ITile]): List[ITile] = {
    if (p1ConcealedTiles.size > 13 || p2ConcealedTiles.size > 13 || p3ConcealedTiles.size > 13 || p4ConcealedTiles.size > 13) {
      throw new IllegalArgumentException("There must be a maximum of 13 tiles by concealed hand")
    }

    val allTiles: scala.collection.mutable.Set[ITile] = TileSets.all.slice(0, 136)

    def mapList(lst: List[ITile]) = {
      lst.map(t => {
        val tile = allTiles.find(t2 => t.getCloneValue == t2.getCloneValue)
        if (tile == None) {
          throw new IllegalStateException("A tile cannot be repeated more than 4 times")
        }
        allTiles -= tile.get
        tile.get
      })
    }

    val tempPlayersTiles: List[List[ITile]] = List(p1ConcealedTiles, p2ConcealedTiles, p3ConcealedTiles, p4ConcealedTiles).
    map(concealedTiles => {

      val pMap: List[ITile] = mapList(concealedTiles)

      val pPadding: List[ITile] =
        (0 until 13 - concealedTiles.size).map({ n =>
          val tileF: Option[ITile] = allTiles.find(t => !wall.contains(t))
          allTiles -= tileF.get
          tileF.get
        }).toList

      pMap ::: pPadding
    })

    val mapWall = mapList(wall)
    val playersTiles: List[ITile] = tempPlayersTiles.map(tilesInHand => {

      val pPadding: List[ITile] =
        (0 until 13 - tilesInHand.size).map({ n =>
          val tileF: ITile = allTiles.find(t=>true).get
          allTiles -= tileF
          tileF
        }).toList

      tilesInHand ::: pPadding
    }).flatten
    
    val remaining = allTiles.toList

    val ret = playersTiles ::: mapWall ::: remaining
    if (ret.size != 136) throw new IllegalStateException("The wall has not 136 tiles, it has:" + ret.size)
    ret.toList
  }

  def makeCrookedGame(p1ConcealedTiles: Option[List[ITile]], p2ConcealedTiles: Option[List[ITile]], p3ConcealedTiles: Option[List[ITile]], p4ConcealedTiles: Option[List[ITile]], wall: Option[List[ITile]]) = {
    val players = Set(
      Player.instanceOf("EAST", EAST),
      Player.instanceOf("NORTH", NORTH),
      Player.instanceOf("WEST", WEST),
      Player.instanceOf("SOUTH", SOUTH))

    new CrookedGame(players, makeCrookedWall(p1ConcealedTiles.getOrElse(Nil), p2ConcealedTiles.getOrElse(Nil), p3ConcealedTiles.getOrElse(Nil), p4ConcealedTiles.getOrElse(Nil), wall.getOrElse(Nil))) with GameHelper
  }

  trait GameHelper extends CrookedGame {
    def current = getLastRound.getCurrentPlayer
    override def instanciateRound(firstPlayer: IPlayer): IRound = {
      new CrookedRound(crookedWall, firstPlayer)
    }

    override def fixmeAddGameObserverToRound = {}
  }
}