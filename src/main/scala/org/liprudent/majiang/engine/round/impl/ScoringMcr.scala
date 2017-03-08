package org.liprudent.majiang.engine.round.impl

import org.slf4j.LoggerFactory
import javax.script.ScriptEngine
import javax.script.ScriptEngineManager
import javax.script.ScriptException

import org.python.core.PySystemState
import org.python.core.Py

import org.liprudent.majiang.engine.player.IPlayerTiles
import org.liprudent.majiang.engine.tile._
import org.liprudent.majiang.engine.tile.impl._
import org.liprudent.majiang.engine.round.Scoring
import org.liprudent.majiang.engine.round.Fan

import scala.collection.JavaConversions._

object PythonEngine {

  val logger = LoggerFactory.getLogger("PythonEngine")
  val engine: ScriptEngine = makeEngine

  private def makeEngine: ScriptEngine = {
    println("PythonEngine.makeEngine")
    val engineSys = new PySystemState
    logger.info("Python path:" + engineSys.path.toString)
    Py.setSystemState(engineSys)
    logger.info("SystemState added")
    val engine = new ScriptEngineManager().getEngineByName("python")
    logger.info("Got engine")
    engine.eval("from majiang import find_mahjong")
    logger.info("import of mahjong done")
    return engine
  }

}


class PyMcrNotationConverter(visibleTiles: Iterable[ITile], playerTiles: IPlayerTiles) {

  type PyMcrNotation = String

  if (visibleTiles == null)
    throw new NullPointerException("visible tiles must be provided")
  if (playerTiles == null)
    throw new NullPointerException("player tiles must be provided")

  def toPyMcrNotation: PyMcrNotation = {
    val sb = new StringBuilder("")

    def convertsets(tiles: Iterable[ITile], sb: StringBuilder, prefix: String, filter: Option[ITile]) = {

      val filteredTiles = if (filter.isDefined) {
        tiles.filterNot(p => p == filter.get)
      } else {
        tiles
      }
      sb.append(" " + prefix + " ")
      filteredTiles.foreach(t => sb.append(toPyMcrNotation(t)))
    }

    val w = playerTiles.getWinningTile.tile
    //melded (open)
    playerTiles.getOpenedHand.foreach(tiles => convertsets(tiles.toList, sb, "m", Some(w)))
    //concealed (hidden)
    playerTiles.getHiddenHand.foreach(tiles => convertsets(tiles.toList, sb, "c", Some(w)))
    //hand (concealed)
    convertsets(playerTiles.getConcealedHand.toList, sb, "h", Some(w))
    // winning Tile
    convertsets(List(w), sb, "w", None)
    //visible tiles
    val opened = playerTiles.getOpenedHand

    // in visible tiles we must not add the winning tile nor the tiles in player's open hand
    val filterVisible = w :: opened.map(tiles => tiles.toArray).flatten.toList
    val visibleTilesWithoutOpenedTiles = visibleTiles.filter(tile => filterVisible.contains(tile) == false)
    convertsets(visibleTilesWithoutOpenedTiles, sb, "v", None)
    sb.toString.trim
  }

  def toPyMcrNotation(tile: ITile): PyMcrNotation = {
    tile match {
      case h: ITileHonor =>
        h.getFamily() match {
          case f@TileFamily.EAST => "We"
          case f@TileFamily.NORTH => "Wn"
          case f@TileFamily.WEST => "Ww"
          case f@TileFamily.SOUTH => "Ws"
          case f@TileFamily.RED => "Dr"
          case f@TileFamily.GREEN => "Dg"
          case f@TileFamily.WHITE => "Dw"
        }
      case s: ITileSuit =>
        s match {
          case _ if s.getFamily() == TileFamily.BAMBOO => "b" + s.getValue().toString
          case _ if s.getFamily() == TileFamily.DOT => "d" + s.getValue().toString
          case _ if s.getFamily() == TileFamily.CHARACTER => "c" + s.getValue().toString
        }
    }
  }
}

object PyMcrNotation {
  def apply(visibleTiles: Iterable[ITile], playerTiles: IPlayerTiles): String = {
    new PyMcrNotationConverter(visibleTiles, playerTiles).toPyMcrNotation
  }
}

object FromPyMcrToTiles {

  type PyMcrNotation = String

  def apply(s: PyMcrNotation): List[ITile] = {
    toTiles(s)
  }

  private def toTiles(s: PyMcrNotation): List[ITile] = {
    val pattern = """(We|Ws|Wn|Ww|Dr|Dg|Dw|b[1-9]|c[1-9]|d[1-9])+?""".r
    val Suit = """([bcd])(\d)""".r

    def tile(s: String): ITile = {
      s match {
        case "We" => new TileHonorEast(0)
        case "Ws" => new TileHonorSouth(0)
        case "Wn" => new TileHonorNorth(0)
        case "Ww" => new TileHonorWest(0)

        case "Dr" => new TileHonorRed(0)
        case "Dg" => new TileHonorGreen(0)
        case "Dw" => new TileHonorWhite(0)

        case Suit(suitType, value) => suitType match {
          case "c" => new TileSuitCharacter(0, value.toInt)
          case "b" => new TileSuitBamboo(0, value.toInt)
          case "d" => new TileSuitDot(0, value.toInt)
        }
      }
    }

    pattern.findAllIn(s).toList.map(tile(_))
  }
}

/**
  * @param visibleTiles is the union of all melded sets and discarded tiles
  **/
class ScoringMcr(visibleTiles: Iterable[ITile], playerTiles: IPlayerTiles) extends Scoring {

  val log = LoggerFactory.getLogger(classOf[ScoringMcr])
  //launch find mahjong
  private val s = PyMcrNotation(visibleTiles, playerTiles)
  log.debug(s);
  PythonEngine.engine.eval("m = find_mahjong('" + s + "')")

  //extract the is_mahjong
  PythonEngine.engine.eval("is_mahjong = m['is_mahjong']")
  val isMahjong: Boolean = getAsBoolean("is_mahjong")

  val (total, fans): Tuple2[Int, List[Fan]] = if (isMahjong) {
    //extract the score
    PythonEngine.engine.eval("score = m['score']")

    (getAsInt("score"), extractFans)
  } else {
    (0, Nil)
  }

  private def extractFans: List[Fan] = {
    //extract the nb of fans
    PythonEngine.engine.eval("nbFans = len(m['fans'])")
    val nbFans: Int = getAsInt("nbFans")

    //extract fans
    var tempFans = new scala.collection.mutable.MutableList[Fan]
    (0 until nbFans).foreach(i => {
      //extract points
      PythonEngine.engine.eval("points = m['fans'][" + i + "]['points']")
      val points: Int = getAsInt("points")
      //extract tiles
      PythonEngine.engine.eval("tiles = m['fans'][" + i + "]['tiles']")
      val tiles = getAsString("tiles")
      val tilesLst = FromPyMcrToTiles(tiles)
      //extract name
      PythonEngine.engine.eval("name = m['fans'][" + i + "]['name']")
      val name = getAsString("name")
      //add to fan list
      tempFans += new Fan(points, name, tilesLst)
    })
    tempFans.toList
  }

  private def getAsInt(pyVar: String): Int = {
    PythonEngine.engine.get(pyVar).asInstanceOf[java.lang.Integer].intValue
  }

  private def getAsString(pyVar: String): String = {
    PythonEngine.engine.get(pyVar).asInstanceOf[java.lang.String]
  }

  private def getAsBoolean(pyVar: String): Boolean = {
    PythonEngine.engine.get(pyVar).asInstanceOf[Boolean]
  }

}


