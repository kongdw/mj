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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import org.junit.Test;
import org.liprudent.majiang.engine.game.IGame;
import org.liprudent.majiang.engine.game.impl.TestConstructHelper;
import org.liprudent.majiang.engine.player.IPlayer;

/**
 * This class groups tests about the fact that a player is iterable
 * 
 * @author jerome
 * 
 */
public class IterablePlayerTest {
	@Test
	public void iterable() {
		final IGame game = TestConstructHelper.constructGame();
		int i = 0;
		for (final IPlayer player : game.getLastRound().getCurrentPlayer()) {
			assertNotNull(player);
			i++;
		}
		assertEquals(4, i);

	}

	@Test
	public void ordered() {
		final IPlayer player = TestConstructHelper.constructGame()
				.getLastRound().getCurrentPlayer();

		final Iterator<IPlayer> iterator = player.iterator();
		assertEquals(player, iterator.next());
		assertEquals(player.getNext(), iterator.next());
		assertEquals(player.getNext().getNext(), iterator.next());
		assertEquals(player.getNext().getNext().getNext(), iterator.next());
		assertFalse(iterator.hasNext());
	}

	@Test
	public void otherPlayersOrdered() {
		final IPlayer current = TestConstructHelper.constructGame()
				.getLastRound().getCurrentPlayer();
		final Iterator<IPlayer> others = current.othersIterator();
		int cptOthers = 0;
		while (others.hasNext()) {
			cptOthers++;
			final IPlayer other = others.next();
			assertNotNull(other);
			assertTrue(other != current);

			if (cptOthers == 1) {
				assertEquals(current.getNext(), other);
			}
			if (cptOthers == 2) {
				assertEquals(current.getNext().getNext(), other);
			}
			if (cptOthers == 3) {
				assertEquals(current.getNext().getNext().getNext(), other);
			}

		}
		assertEquals(3, cptOthers);
	}
}
