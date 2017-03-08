package org.liprudent.majiang.engine.event

import org.liprudent.majiang.engine.player.IPlayerTiles
import org.liprudent.majiang.engine.round.Scoring
trait IEndRoundEvent extends IEvent {
	val scoring:Scoring
	val tiles:IPlayerTiles
}