package org.liprudent.majiang.engine.event.impl

import org.liprudent.majiang.engine.event.NoBrodcastEvent
import org.liprudent.majiang.engine.event.IGameOverEvent
import org.liprudent.majiang.engine.event.IEvent.KindOfEvent
import org.liprudent.majiang.engine.player.IPlayer.Wind
import org.liprudent.majiang.engine.event.IEndRoundDrawEvent
class GameOverEvent (name:String,wind:Wind) extends BasicEvent(name,wind) with IGameOverEvent with NoBrodcastEvent{
	override def publicView = throw new IllegalStateException("This method should not be called. There is one instance of this event per player.")
	val kind = KindOfEvent.GAME_OVER
	override def  getKind = kind
}