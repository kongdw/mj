/**
 * Majiang is a library that implements Mahjong game rules.
 *
 * Copyright 2009 Prudent Jérome
 *
 *     This file is part of Majiang.
 *
 *     Majiang is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Majiang is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 *
 *
 * You can contact me at jprudent@gmail.com
 */
package org.liprudent.majiang.engine.round;

import java.util.List;

import org.liprudent.majiang.engine.player.IPlayer;
import org.liprudent.majiang.engine.tile.ITile;
import org.liprudent.majiang.engine.tile.ITileSets;

/**
 * A round is ending when no tiles left in the wall.
 *  牌局
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
