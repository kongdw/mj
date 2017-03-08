package org.liprudent.majiang.engine.round;

import java.util.List;

import org.liprudent.majiang.engine.player.IPlayer;
import org.liprudent.majiang.engine.tile.ITile;
import org.liprudent.majiang.engine.tile.ITileSets;

/**
 * A round is ending when no tiles left in the wall.
 *  回合
 * @author jerome
 * 
 */
public interface IRound extends IRoundAction {
	/**
	 * @return The player that starts the round (he gets an extra tile)
	 */
	IPlayer getCurrentPlayer();

	/**
	 * @return Other players (not the current)
	 */
	List<IPlayer> getOtherPlayers();

	/**
	 * @return Tile sets
	 */
	ITileSets getTileSet();

	/**
	 * @return true 一局结束
	 */
	boolean doEnd();

	/**
	 * @return The winner, null if no winner.
	 */
	IPlayer getWinner();
	
	/**
	 * 获取得分
	 * @return The score
	 */
	Scoring getScoring();

	/**
	 * @return All visible tiles (discarded + opened)
	 */
	List<ITile> getVisibleTiles();

}
