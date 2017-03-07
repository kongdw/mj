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
import java.util.Collections;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.collections.CollectionUtils;
import org.liprudent.majiang.engine.event.impl.EventFactory;
import org.liprudent.majiang.engine.player.IPlayerTiles;
import org.liprudent.majiang.engine.player.WinningTile;
import org.liprudent.majiang.engine.tile.ITile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;

/**
 * This class is dedicated in manipulating Tiles of a player
 * 
 * @author jerome
 * 
 */
public class PlayerTiles implements IPlayerTiles, IPlayerTilesActions {
	private final SortedSet<ITile> concealedHand = Sets.newTreeSet();
	private final Set<SortedSet<ITile>> hiddenHand = Sets.newHashSet();
	private final Set<SortedSet<ITile>> openedHand = Sets.newHashSet();
	private WinningTile winningTile = null;
	private final Logger log = LoggerFactory.getLogger(PlayerTiles.class);

	@Override
	public void giveTileInConcealedHand(final ITile tile) {
		Preconditions.checkArgument(!concealedHand.contains(tile), "Tile :"
				+ tile + "already exists in concealedHand" + concealedHand);
		concealedHand.add(tile);
		winningTile = new WinningTile(tile, false, false, false, false);
	}

	@Override
	public void giveTilesInConcealedHand(final Collection<ITile> tiles) {
		Preconditions.checkArgument(!CollectionUtils.containsAny(tiles,
				concealedHand), "Some tiles: " + tiles
				+ " already exists in concealedHand:" + concealedHand);
		for (ITile tile : tiles) {
			giveTileInConcealedHand(tile);
		}
	}

	private SortedSet<ITile> allTilesInOpenedHand() {
		SortedSet<ITile> ret = new TreeSet<ITile>();
		for (SortedSet<ITile> set : openedHand) {
			ret.addAll(set);
		}
		return ret;
	}

	@Override
	public void moveFromConcealedToOpen(final Collection<ITile> tiles) {
		Preconditions.checkArgument(concealedHand.containsAll(tiles),
				"Some tiles doesn't exist in concealedHand");
		Preconditions.checkArgument(!CollectionUtils.containsAny(tiles,
				allTilesInOpenedHand()),
				"Some tiles are already in opened hand");
		concealedHand.removeAll(tiles);
		openedHand.add(Sets.newTreeSet(tiles));
	}

	@Override
	public void moveFromConcealedToHidden(final Collection<ITile> tiles) {
		Preconditions.checkArgument(concealedHand.containsAll(tiles),
				"Some tiles doesn't exist in concealedHand");
		Preconditions.checkArgument(!CollectionUtils.containsAny(tiles,
				hiddenHand), "Some tiles are already in hidden hand");
		concealedHand.removeAll(tiles);
		hiddenHand.add(Sets.newTreeSet(tiles));

	}

	@Override
	public void moveFromConcealedToOpen(final ITile tile,
			final Collection<ITile> pung) {
		Preconditions.checkArgument(concealedHand.contains(tile),
				"Tile doesn't exist in concealedHand");
		for (final Collection<ITile> c : openedHand) {
			if (c.containsAll(pung)) {
				c.add(tile);
				concealedHand.remove(tile);
				return;
			}
		}
		throw new IllegalArgumentException(
				"The pung is not fund in opened hand");
	}

	@Override
	public void discardTile(final ITile throwedTile) {
		Preconditions.checkArgument(concealedHand.contains(throwedTile),
				"Tile doesn't exist in concealedHand");
		concealedHand.remove(throwedTile);
		Preconditions.checkArgument(!concealedHand.contains(throwedTile),
				"Tile " + throwedTile + "still exists in concealedHand");

	}

	@Override
	public SortedSet<ITile> getConcealedHand() {
		return Collections.unmodifiableSortedSet(concealedHand);
	}

	@Override
	public Set<SortedSet<ITile>> getHiddenHand() {
		//TODO: make elements unmodifiable too
		return Collections.unmodifiableSet(hiddenHand);
	}

	@Override
	public Set<SortedSet<ITile>> getOpenedHand() {
		//TODO: make elements unmodifiable too
		return Collections.unmodifiableSet(openedHand);
	}

	@Override
	public WinningTile getWinningTile() {
		return winningTile;
	}

	@Override
	public void setWinningTile(ITile tile) {
		//TODO set real value instead of false,false,...
		winningTile = new WinningTile(tile, false, false, false, false);
		log.debug("Winning tile set : " + winningTile);
	}

	@Override
	public void moveTileInOpen(ITile tile, SortedSet<ITile> into) {
		boolean found = false;
		for (SortedSet<ITile> set : getOpenedHand()) {
			if (set.equals(into)) {
				set.add(tile);
				found = true;
				break;
			}
		}
		if (!found) {
			throw new IllegalArgumentException("The tiles : " + into
					+ "cannot be found in open hand");
		}
		
	}

	@Override
	public String toString() {
		return "PlayerTiles [concealedHand=" + concealedHand + ", hiddenHand="
				+ hiddenHand + ", openedHand=" + openedHand + ", winningTile="+winningTile+"]";
	}
	
	
}