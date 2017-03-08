package org.liprudent.majiang.engine.round.impl;

import java.io.Serializable;

import org.liprudent.majiang.engine.player.IPlayer;
import org.liprudent.majiang.engine.round.Scoring;
import org.liprudent.majiang.engine.round.impl.treatment.ITreatment;

/**
 * A turn begin with a player having 14 tiles, throwing. Others can pass or eat.
 * That'all.
 * 回合
 * @author jerome
 * 
 */
public interface ITurn extends Serializable{
	/**
	 * @param treatment
	 * @return true if the treatment is valid
	 */
	boolean processTreatment(final ITreatment treatment);

	boolean endTurn();

	boolean endRoundBecauseMahjong();

	/**
	 * @return The "turn winner" is the player who eats the discarded tile. Null
	 * otherwise (everybody pass).
	 */
	IPlayer getTurnWinner();

	/**
	 * @return The score of the winner
	 */
	Scoring getScoring();
	
	/**
	 * @return true if the turn winner won with a kong
	 */
	boolean isKongTurnWinner();
}
