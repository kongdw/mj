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
package org.liprudent.majiang.engine.event.impl;

import java.util.Collection;
import java.util.SortedSet;

import org.liprudent.majiang.engine.event.GameInfo;
import org.liprudent.majiang.engine.event.IEvent;
import org.liprudent.majiang.engine.event.KindOfHand;
import org.liprudent.majiang.engine.event.KindOfWall;
import org.liprudent.majiang.engine.player.IPlayer.Wind;
import org.liprudent.majiang.engine.round.State;
import org.liprudent.majiang.engine.tile.ITile;

import com.google.common.collect.Sets;

public final class EventFactory {
	private EventFactory() {
	}

	public static IEvent instanceOf(final String name, final Wind wind) {
		return new WindEvent(name, wind);
	}

	public static IEvent instanceOf(final String name, final Wind wind,
			final SortedSet<ITile> tiles, final KindOfWall source,
			final KindOfHand destination) {
		return new TilesGivenCurrentEvent(name, wind, source, destination,
				tiles);
	}

	public static IEvent instanceOf(final String name, final Wind wind,
			final ITile tile, final KindOfWall source,
			final KindOfHand destination) {
		SortedSet<ITile> tiles = Sets.newTreeSet();
		tiles.add(tile);
		return new TilesGivenCurrentEvent(name, wind, source, destination, tiles);
	}

	public static IEvent instanceOf(final String name, final Wind wind,
			final Collection<ITile> tiles, final KindOfHand destination) {
		return new MovedTilesFromConcealedEvent(name, wind, destination, tiles);
	}

	public static IEvent instanceOf(final String name, final Wind wind,
			final ITile discardedTile) {
		return new DiscardedTileEvent(name, wind, discardedTile);
	}

	public static IEvent instanceOf(final String name, final Wind wind,
			final State state) {

		return new PlayerStateChangedEvent(name, wind, state);
	}

	public static IEvent instanceOf(String name, GameInfo gameInfo) {
		return new JoinEvent(name, null, gameInfo);
	}
	
	public static IEvent instanceOf(final String name, final Wind wind,
			final ITile discardedTile, final Wind discarder, final SortedSet<ITile> into) {
		return new MoveDiscardedTileInOpenEvent(name, wind, discardedTile,into,discarder);
	}

	public static IEvent instanceOf(final String name, final Wind wind, final ITile tile,
			final SortedSet<ITile> pung) {
		return new MoveTileInOpenKongEvent(name, wind,tile,pung);
	}
}
