package org.liprudent.majiang.engine.event.impl

import org.liprudent.majiang.engine.event.IEvent.KindOfEvent
import org.liprudent.majiang.engine.event.IMoveDiscardedTileInOpenEvent
import org.liprudent.majiang.engine.tile.ITile
import java.util.SortedSet
import org.liprudent.majiang.engine.player.IPlayer.Wind
import org.liprudent.majiang.engine.round.Scoring
import org.liprudent.majiang.engine.event.IEndRoundEvent
import org.liprudent.majiang.engine.player.IPlayerTiles
class MoveDiscardedTileInOpenEvent(name:String,wind:Wind,override val discardedTile:ITile, override val into:SortedSet[ITile], override val discarderWind:Wind) extends BasicEvent(name,wind) with IMoveDiscardedTileInOpenEvent{
	override def publicView = this
	val kind = KindOfEvent.MOVE_DISCARDED_TO_OPEN
	override def  getKind = kind
}

