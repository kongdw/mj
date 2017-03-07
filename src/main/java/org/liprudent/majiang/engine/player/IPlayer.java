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
package org.liprudent.majiang.engine.player;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.SortedSet;

import org.liprudent.common.IObservable;
import org.liprudent.majiang.engine.event.GameInfo;
import org.liprudent.majiang.engine.event.KindOfWall;
import org.liprudent.majiang.engine.round.State;
import org.liprudent.majiang.engine.tile.ITile;

/**
 * This interface extends Iterable so that one can iterate over the 4 players.
 * The hasNext() method returns false when cycling on this.
 * 
 * @author jerome
 * 
 */
public interface IPlayer extends Iterable<IPlayer>, IObservable, Serializable {
	enum Wind {
		EAST, NORTH, WEST, SOUTH;

		public Wind getNextWind() {
			if (this == EAST) {
				return NORTH;
			}
			if (this == NORTH) {
				return WEST;
			}
			if (this == WEST) {
				return SOUTH;
			}
			if (this == SOUTH) {
				return EAST;
			}
			throw new AssertionError("Shouldn't go here");
		}

	}
	
	//called when a player has joined a game
	void joinGame(GameInfo gameInfo);
	
	State getState();

	/**
	 * FIXME shouldn't appear in interface
	 * 
	 * @param state
	 */
	void setState(final State state);

	Wind getWind();

	/**
	 * FIXME shouldn't appear in interface
	 * 
	 * @param wind
	 */
	void setWind(Wind wind);

	/**
	 * FIXME shouldn't appear in interface
	 * 
	 * @param player
	 */
	void setNext(IPlayer player);

	/**
	 * FIXME shouldn't appear in interface
	 * 
	 * @param player
	 */
	void setPrevious(IPlayer player);

	/**
	 * @return The player after this player. Previous player of next player must
	 *         be this.
	 */
	IPlayer getNext();

	/**
	 * @return The player before this player. Next player of next player must be
	 *         this.
	 */
	IPlayer getPrevious();

	String getName();

	/**
	 * @return an iterator on other players
	 */
	Iterator<IPlayer> othersIterator();

	/**
	 * !!! This method doesn't modify tileset
	 * 
	 * @param tile
	 *            Add the tile to the concealed hand
	 * @param from
	 *            From where the tile comes from
	 */
	void giveTileInConcealedHand(ITile tile, KindOfWall from);

	/**
	 * !!! This method doesn't modify tileset
	 * 
	 * @param tiles
	 *            Add the tiles to the concealed hand
	 * @param from
	 *            From where the tile comes from
	 */
	void giveTilesInConcealedHand(SortedSet<ITile> tiles, KindOfWall from);

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
	 * @param tile The discarded tile
	 * @param tilesInOpenHand where to add the discarded tile
	 * @param discarder the wind of the player who discarded the tile
	 */
	void moveDiscardedTileInOpen(ITile tile, SortedSet<ITile> tilesInOpenHand,
			Wind discarder);
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
	 * Delete all tiles from all hands
	 */
	void clearAllTiles();
	
	IPlayerTiles getTiles();


}
