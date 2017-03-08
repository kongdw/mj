package org.liprudent.majiang.engine.event.impl

import org.liprudent.majiang.engine.event.IEvent.KindOfEvent
import org.liprudent.majiang.engine.player.IPlayer.Wind
import org.liprudent.majiang.engine.round.Scoring
import org.liprudent.majiang.engine.event.IEndRoundEvent
import org.liprudent.majiang.engine.player.IPlayerTiles
class EndRoundEvent(name:String,wind:Wind,override val scoring:Scoring,override val tiles:IPlayerTiles) extends BasicEvent(name,wind) with IEndRoundEvent{
	override def publicView = this
	val kind = KindOfEvent.SCORE
	override def  getKind = kind
}