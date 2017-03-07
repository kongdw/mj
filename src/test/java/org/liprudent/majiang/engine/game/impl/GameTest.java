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
package org.liprudent.majiang.engine.game.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.unitils.util.ReflectionUtils.getFieldValue;
import static org.unitils.util.ReflectionUtils.getFieldWithName;

import java.util.Collection;

import junit.framework.Assert;

import org.junit.Test;
import org.liprudent.majiang.engine.event.IEvent;
import org.liprudent.majiang.engine.event.IEventLogger;
import org.liprudent.majiang.engine.event.IEventObserver;
import org.liprudent.majiang.engine.event.IWindEvent;
import org.liprudent.majiang.engine.player.IPlayer;

import com.google.common.collect.Sets;

public class GameTest {
	@Test
	public void constructor() {
		final Game game = (Game) TestConstructHelper.constructGame();
		// FIXME objectives of a game is only to instanciate rounds

		checkPlayersHasBeenInitialized(game);

		// Each player has his own event logger
		final IEventObserver observer = getFieldValue(game, getFieldWithName(
				Game.class, "eventsObserver", false));
		assertNotNull(observer.eventLogger(TestConstructHelper.PLAYER_1));
		assertNotNull(observer.eventLogger(TestConstructHelper.PLAYER_2));
		assertNotNull(observer.eventLogger(TestConstructHelper.PLAYER_3));
		assertNotNull(observer.eventLogger(TestConstructHelper.PLAYER_4));
	}

	private void checkPlayersHasBeenInitialized(final Game game) {
		final Collection<IPlayer> players = getFieldValue(game,
				getFieldWithName(Game.class, "players", false));
		for (final IPlayer player : players) {
			Assert.assertNotNull(player.getWind());
			Assert.assertNotNull(player.getNext());
			Assert.assertNotNull(player.getPrevious());
			Assert.assertEquals(player.getNext().getPrevious(), player);
			Assert.assertEquals(player.getPrevious().getNext(), player);
			Assert.assertEquals(player.getWind().getNextWind(), player
					.getNext().getWind());
		}
	}

	@Test
	public void constructorAllPlayersNamesAreDifferent() {
		GameFactory.instanceOf(Sets.newHashSet("Player 1", "Player 2", "Player 3",
				"Player 4"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void constructorAllPlayersNamesAreEquals() {
		GameFactory.instanceOf(Sets.newHashSet("Player 1", "Player 1", "Player 2",
				"Player 3"));
	}

	@Test
	public void check4WindEvents() {
		final Game game = (Game) TestConstructHelper.constructGame();
		// Must have at least 4 events (wind)
		final IEventLogger events = game
				.getEvents(TestConstructHelper.PLAYER_1);
		assertTrue("There are only " + events.nbUnread() + "events", events
				.nbUnread().intValue() >= 4);

		// Check all winds are here
		int cptWindEvents = 0;
		for (final IEvent evt : events.unreads()) {
			if (evt instanceof IWindEvent) {
				cptWindEvents++;
			}
		}
		assertEquals(4, cptWindEvents);
	}
}
