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
package org.liprudent.majiang.event;

import static org.junit.Assert.assertNotNull;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.liprudent.majiang.engine.event.impl.EventObserver;
import org.liprudent.majiang.engine.player.IPlayer;
import org.liprudent.majiang.engine.player.impl.Player;

public class EventObserverTest {
	@Test(expected = java.lang.NullPointerException.class)
	public void constructorNPE() {
		new EventObserver(null);
	}

	@Test(expected = java.lang.IllegalArgumentException.class)
	public void constructorAssertSize() {
		new EventObserver(new HashSet<IPlayer>());
	}

	@Test
	public void constructor() {
		final Set<IPlayer> players = new HashSet<IPlayer>();
		final Player a = Player.instanceOf("A");
		players.add(a);
		final Player b = Player.instanceOf("B");
		players.add(b);
		final Player c = Player.instanceOf("C");
		players.add(c);
		final Player d = Player.instanceOf("D");
		players.add(d);
		final EventObserver eventObserver = new EventObserver(players);
		assertNotNull(eventObserver.eventLogger("A"));
		assertNotNull(eventObserver.eventLogger("B"));
		assertNotNull(eventObserver.eventLogger("C"));
		assertNotNull(eventObserver.eventLogger("D"));

		assertNotNull(a.countObservers() == 1);
		assertNotNull(b.countObservers() == 1);
		assertNotNull(c.countObservers() == 1);
		assertNotNull(d.countObservers() == 1);
	}

}
