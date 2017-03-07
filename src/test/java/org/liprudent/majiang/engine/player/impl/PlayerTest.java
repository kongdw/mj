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

import java.util.Observable;
import java.util.Observer;
import java.util.SortedSet;

import org.junit.Assert;
import org.junit.Test;
import org.liprudent.majiang.engine.event.IEvent;
import org.liprudent.majiang.engine.event.IPlayerStateChangedEvent;
import org.liprudent.majiang.engine.event.KindOfHand;
import org.liprudent.majiang.engine.event.KindOfWall;
import org.liprudent.majiang.engine.event.impl.EventFactory;
import org.liprudent.majiang.engine.event.impl.PlayerStateChangedEvent;
import org.liprudent.majiang.engine.game.impl.TestConstructHelper;
import org.liprudent.majiang.engine.player.IPlayer;
import org.liprudent.majiang.engine.player.IPlayer.Wind;
import org.liprudent.majiang.engine.round.State;
import org.liprudent.majiang.engine.tile.ITile;

public class PlayerTest {
	@Test
	public void constructor() {
		final IPlayer player = Player.instanceOf("Turing", Wind.NORTH);
		// new HashSet<ITile>(), null, null);
		Assert.assertEquals(0, player.getTiles().getHiddenHand().size());
		Assert.assertEquals(0, player.getTiles().getOpenedHand().size());
	}

	@Test(expected = NullPointerException.class)
	public void constructorNameMandatory() {
		Player.instanceOf(null, Wind.NORTH);
	}

	/**
	 * 
	 */

	@Test
	public void equal() {
		assertEquals(Player.instanceOf("ciboulette"), Player
				.instanceOf("ciboulette"));
		assertFalse(Player.instanceOf("ciboulette").equals(
				Player.instanceOf("estragon")));
	}

	@Test
	public void setState() {
		final IPlayer p = Player.instanceOf(TestConstructHelper.PLAYER_1);
		final TestableObserver testableObserver = new TestableObserver();
		p.addObserver(testableObserver);

		p.setState(State.END);

		final IPlayerStateChangedEvent mustBe = new PlayerStateChangedEvent(p
				.getName(), p.getWind(), State.END);

		assertEquals(mustBe, testableObserver.event);
	}

	@Test
	public void setWind() {
		final IPlayer p = Player.instanceOf(TestConstructHelper.PLAYER_1);
		final TestableObserver testableObserver = new TestableObserver();
		p.addObserver(testableObserver);

		p.setWind(Wind.EAST);
		final IEvent mustBe = EventFactory.instanceOf(
				TestConstructHelper.PLAYER_1, Wind.EAST);
		assertEquals(mustBe, testableObserver.event);
	}

	class TestableObserver implements Observer {
		public IEvent event;

		@Override
		public void update(final Observable o, final Object arg) {
			event = (IEvent) arg;
		}
	};

	@Test
	public void giveTilesInConcealedHandEvent() {
		final IPlayer p = Player.instanceOf(TestConstructHelper.PLAYER_1);
		final TestableObserver testableObserver = new TestableObserver();
		p.addObserver(testableObserver);

		final SortedSet<ITile> set = TestConstructHelper.set("S0,S1");
		p.giveTilesInConcealedHand(set, KindOfWall.WALL);

		final IEvent mustBe = EventFactory.instanceOf(
				TestConstructHelper.PLAYER_1, null, set, KindOfWall.WALL,
				KindOfHand.CONCEALED);
		assertEquals(mustBe, testableObserver.event);
	}

	@Test
	public void giveTileInConcealedHandEvent() {
		final IPlayer p = Player.instanceOf(TestConstructHelper.PLAYER_1);
		final TestableObserver testableObserver = new TestableObserver();
		p.addObserver(testableObserver);

		final ITile tile = TestConstructHelper.tile("S0");
		p.giveTileInConcealedHand(tile, KindOfWall.WALL);

		final IEvent mustBe = EventFactory.instanceOf(
				TestConstructHelper.PLAYER_1, null, tile, KindOfWall.WALL,
				KindOfHand.CONCEALED);
		assertEquals(mustBe, testableObserver.event);
	}

}
