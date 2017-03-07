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
package org.liprudent.majiang.engine.player.impl;

import java.util.Collection;
import java.util.SortedSet;

import org.liprudent.majiang.engine.player.IPlayer.Wind;
import org.liprudent.majiang.engine.tile.ITile;

public interface IPlayerTilesActions {
	
	/**
	 * !!! This method doesn't modify tileset
	 * 
	 * @param tile
	 *            Add the tile to the concealed hand
	 */
	void giveTileInConcealedHand(ITile tile);

	/**
	 * !!! This method doesn't modify tileset
	 * 
	 * @param tiles
	 *            Add the tiles to the concealed hand
	 */
	void giveTilesInConcealedHand(Collection<ITile> tiles);

	/**
	 * @param tiles
	 *            Extract tiles from concealed hand to opened hand
	 */
	void moveFromConcealedToOpen(Collection<ITile> tiles);

	/**
	 * @param tiles
	 *            Extract tiles from concealed hand to hidden hand
	 */
	void moveFromConcealedToHidden(Collection<ITile> tiles);

	/**
	 * This method should be called in only one case : when player declare a
	 * kong with a previous pung and a concealed tile
	 * 
	 * @param tile
	 *            The concealed tile
	 * @param pung
	 *            A previous pung which is in opened hand
	 */
	void moveFromConcealedToOpen(ITile tile, Collection<ITile> pung);

	/**
	 * !!! This method doesn't modify tileset
	 * 
	 * @param throwedTile
	 *            A tile in concealed hand to be removed
	 * 
	 */
	void discardTile(ITile throwedTile);
	
	
	/**
	 * This method is called to add a tile in a set of open hand (use case: eat action)
	 * @param tile
	 * @param into
	 */
	public void moveTileInOpen(ITile tile,SortedSet<ITile> into);
}
