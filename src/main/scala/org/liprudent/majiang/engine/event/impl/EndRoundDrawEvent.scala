package org.liprudent.majiang.engine.event.impl

import org.liprudent.majiang.engine.event.IEvent.KindOfEvent
import org.liprudent.majiang.engine.event.NoBrodcastEvent
import org.liprudent.majiang.engine.player.IPlayer.Wind
import org.liprudent.majiang.engine.event.IEndRoundDrawEvent
class EndRoundDrawEvent(name:String,wind:Wind) extends BasicEvent(name,wind) with IEndRoundDrawEvent with NoBrodcastEvent{
	override def publicView = throw new IllegalStateException("This method should not be called. There is one instance of this event per player.")
	val kind = KindOfEvent.DRAW
	override def  getKind = kind
}