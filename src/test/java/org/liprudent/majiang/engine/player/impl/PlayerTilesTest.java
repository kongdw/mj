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

import static org.apache.commons.collections.CollectionUtils.containsAny;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.liprudent.majiang.engine.game.impl.TestConstructHelper;
import org.liprudent.majiang.engine.tile.ITile;

public class PlayerTilesTest {
	PlayerTiles playerTiles;

	@Before
	public void before() {
		playerTiles = new PlayerTiles();
	}

	@Test
	public void constructor() {
		assertEmpty(playerTiles.getConcealedHand());
		assertEmpty(playerTiles.getHiddenHand());
		assertEmpty(playerTiles.getOpenedHand());
	}

	private static void assertEmpty(final Collection<?> hand) {
		assertNotNull("hand is null", hand);
		assertEquals("hand is not empty", 0, hand.size());
	}

	@Test
	public void giveTilesInConcealedHand() {
		final Collection<ITile> mustBe = TestConstructHelper.set("S0,S1");
		playerTiles.giveTilesInConcealedHand(mustBe);
		assertTrue(playerTiles.getConcealedHand().containsAll(mustBe));
	}

	@Test
	public void giveTileInConcealedHand() {
		final ITile mustBe = TestConstructHelper.tile("S0");
		playerTiles.giveTileInConcealedHand(mustBe);
		assertTrue(playerTiles.getConcealedHand().contains(mustBe));
	}

	@Test
	public void moveFromConcealedToOpen() {
		final Collection<ITile> mustBe = TestConstructHelper.set("S0,S1");
		playerTiles.giveTilesInConcealedHand(mustBe);

		playerTiles.moveFromConcealedToOpen(mustBe);

		assertTrue(playerTiles.getOpenedHand().contains(mustBe));
		assertFalse(playerTiles.getConcealedHand().containsAll(mustBe));
	}

	@Test
	public void moveFromConcealedToOpenSingleTile() {
		final Collection<ITile> pung = TestConstructHelper.set("S0,S1,S2");
		playerTiles.giveTilesInConcealedHand(pung);
		playerTiles.moveFromConcealedToOpen(pung);
		final ITile tile = TestConstructHelper.tile("S3");
		playerTiles.giveTileInConcealedHand(tile);

		playerTiles.moveFromConcealedToOpen(tile, pung);

		pung.add(tile);
		final Collection<ITile> kong = pung;

		assertTrue(playerTiles.getOpenedHand().iterator().next().containsAll(
				kong));
		assertFalse(containsAny(playerTiles.getConcealedHand(), kong));
	}

	@Test
	public void moveFromConcealedToHidden() {
		final Collection<ITile> mustBe = TestConstructHelper.set("S0,S1");
		playerTiles.giveTilesInConcealedHand(mustBe);

		playerTiles.moveFromConcealedToHidden(mustBe);

		assertTrue(playerTiles.getHiddenHand().contains(mustBe));
		assertFalse(playerTiles.getConcealedHand().containsAll(mustBe));
	}

	@Test
	public void discardTile() {
		final ITile discardedTile = TestConstructHelper.tile("S3");
		playerTiles.giveTileInConcealedHand(discardedTile);

		playerTiles.discardTile(discardedTile);

		assertFalse(playerTiles.getConcealedHand().contains(discardedTile));
	}
}
