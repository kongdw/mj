package org.liprudent.majiang.engine.event.impl

import org.liprudent.majiang.engine.event.IMoveTileInOpenKongEvent
import org.liprudent.majiang.engine.event.IEvent.KindOfEvent
import java.util.SortedSet
import org.liprudent.majiang.engine.tile.ITile
import org.liprudent.majiang.engine.player.IPlayer.Wind

class MoveTileInOpenKongEvent(name: String, wind: Wind, override val ownedTile: ITile, override val pung: SortedSet[ITile]) extends BasicEvent(name, wind) with IMoveTileInOpenKongEvent {
	override def publicView = this
	val kind = KindOfEvent.MOVE_TILE_IN_OPEN_KONG
	override def  getKind = kind
}