package org.liprudent.majiang.engine.event

import org.liprudent.majiang.engine.tile.ITileHonor
import org.liprudent.majiang.engine.tile.ITileSuit
import org.liprudent.majiang.engine.tile.ITile
import org.liprudent.majiang.engine.player.IPlayer
import scala.collection.JavaConversions._

object JsonEventConverter {
  def apply(event: IEvent): String = {
    val jsObj = event match {
      case j: IJoinEvent => JsObj(("kind", "GAME_OPTIONS"), ("name", j.getName), ("gameInfo", JsObj(("wallSize", j.getGameInfo.getWallSize))))
      case w: IWindEvent => JsObj(("kind", "WIND"), ("name", w.getName), ("wind", w.getPlayerWind.toString))
      case c: ITilesGivenEvent => JsObj(("kind", "CONCEALED"),  ("name", c.getName), ("wind", c.getPlayerWind.name), ("source", c.getSource.toString), ("tiles", mkTilesObj(c.getTiles.toList)))
      case co: ITilesGivenPublicEvent => JsObj(("kind", "CONCEALED_OTHER"), ("name", co.getName), ("wind", co.getPlayerWind.name), ("source", co.getSource.toString), ("nbTiles", co.getNbTiles))
      case s: IPlayerStateChangedEvent => JsObj(("kind", "STATE"), ("name", s.getName), ("wind", s.getPlayerWind.name), ("state", s.getState.name))
      case d: IDiscardedTile => JsObj(("kind", "DISCARDED"), ("name", d.getName), ("wind", d.getPlayerWind.name), ("tile", mkTileObj(d.getTile)))
      case m: IMovedTilesFromConcealed => JsObj(("kind", "MOVED_FROM_CONCEALED"), ("destination", m.getDestination.toString), ("name", m.getName), ("wind", m.getPlayerWind.name), ("tiles", mkTilesObj(m.getTiles)))
      case m: IMovedTilesFromConcealedToHiddenPublicEvent => JsObj(("kind", "MOVED_FROM_CONCEALED_TO_HIDDEN_OTHER"), ("name", m.getName), ("wind", m.getPlayerWind.name), ("nbTiles", m.getNbTiles))
      case d: IMoveDiscardedTileInOpenEvent => JsObj(("kind", "MOVE_DISCARDED_TO_OPEN"), ("name", d.getName), ("wind", d.getPlayerWind.name), ("tile", mkTileObj(d.discardedTile)), ("into", mkTilesObj(d.into)), ("discarder", d.discarderWind.toString))
      case d: IMoveTileInOpenKongEvent => JsObj(("kind", "MOVE_TILE_IN_OPEN_KONG"), ("name", d.getName), ("wind", d.getPlayerWind.name), ("tile", mkTileObj(d.ownedTile)), ("pung", mkTilesObj(d.pung)))
      case e: IEndRoundEvent =>
        JsObj(("kind", "SCORE"), ("name", e.getName), ("wind", e.getPlayerWind.name), ("score", e.scoring.total),
          ("fans", new JsArray(e.scoring.fans.map(f => JsObj(("points", f.points), ("name", f.name), ("tiles", mkTilesObj(f.tiles)))))))
      case d: IEndRoundDrawEvent => JsObj(("kind", "DRAW"))
      case d: IGameOverEvent => JsObj(("kind", "GAME_OVER"))
      case x => JsObj(("kind", "UNKNOWN"), ("detail", x.toString))
    }
    jsObj.toString
  }

  trait JsExp {}

  private def mkTilesObj(tiles: Iterable[ITile]): JsArray = {
    new JsArray(tiles.map { t => mkTileObj(t) })
  }

  class JsArray(val members: Iterable[JsExp]) extends JsExp {
    override def toString: String = {
      members.map { j => j.toString }.mkString("[", ",", "]")
    }
  }

  class JsObj(val members: Iterable[(String, Any)]) extends JsExp {
    override def toString: String = {
      members.map({
        case (a, b) =>
          b match {
            case b: String => "\"" + a.toString + "\":" + "\"" + b.toString + "\""
            case _ => "\"" + a.toString + "\":" + b.toString
          }
      }).mkString("{", ",", "}")
    }
  }

  object JsObj {
    def apply(members: (String, Any)*): JsObj = new JsObj(members.toList)
  }

  private def mkTileObj(tile: ITile): JsObj = {
    tile match {
      case h: ITileHonor => mkTileObj(h.getFamily.name(), 0, h.getId)
      case s: ITileSuit => mkTileObj(s.getFamily().name(), s.getValue(), s.getId)
    }
  }

  def mkTileObj(family: String, value: Int, id: Int) = {
    JsObj(("family", family), ("value", value), ("id", id))
  }
}