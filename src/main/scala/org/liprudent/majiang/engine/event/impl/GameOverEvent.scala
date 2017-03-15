package org.liprudent.majiang.engine.event.impl

import org.liprudent.majiang.engine.event.{IEndRoundDrawEvent, IGameOverEvent, NoBroadcastEvent}
import org.liprudent.majiang.engine.event.IEvent.KindOfEvent
import org.liprudent.majiang.engine.player.IPlayer.Wind
class GameOverEvent (name:String,wind:Wind) extends BasicEvent(name,wind) with IGameOverEvent with NoBroadcastEvent{
	override def publicView = throw new IllegalStateException("This method should not be called. There is one instance of this event per player.")
	val kind = KindOfEvent.GAME_OVER
	override def  getKind = kind
}