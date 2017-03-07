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
package org.liprudent.majiang.engine.tile.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.liprudent.majiang.engine.game.impl.TestConstructHelper;
import org.liprudent.majiang.engine.tile.ITile;
import org.liprudent.majiang.engine.tile.TileFamily;
import static org.liprudent.majiang.engine.tile.TileFamily.FOO;
import org.liprudent.majiang.engine.tile.TileKind;

public class TileSetTest {
	private static Set<ITile> tileSet = TileSets.getAll();

	@Test
	public void tileSetNumber() {
		assertEquals(136, tileSet.size());
	}

	@Test
	public void idNumber() {
		for (final ITile tile : tileSet) {
			final boolean condition = tile.getId() >= 0 && tile.getId() <= 3;
			assertTrue(tile + ":id is not between 0 an 3", condition);
		}
	}

	@Test
	public void thereAre4Or36OfEachKindOfTile() {
		final Map<TileFamily, Integer> counterGeneral = new HashMap<TileFamily, Integer>();
		for (final ITile tile : tileSet) {
			final Integer count = counterGeneral.get(tile.getFamily());
			if (count == null) {
				counterGeneral.put(tile.getFamily(), 1);
			} else {
				counterGeneral.put(tile.getFamily(), count + 1);
			}
		}
		for (final TileFamily family : TileFamily.values()) {
			if (family == FOO) {
				assertEquals(null, counterGeneral.get(family));
			} else if (family.getTileKind() == TileKind.HONOR)
				assertEquals(Integer.valueOf(4), counterGeneral.get(family));
			else if (family.getTileKind() == TileKind.SUIT)
				assertEquals(Integer.valueOf(36), counterGeneral.get(family));
			else
				fail("unknown kind");
		}
	}

	@Test
	public void constructor() {
		final TileSets tileSet = new TileSets();
		assertEquals(13, tileSet.getDeadWall().size());
		assertEquals(123, tileSet.getWall().size());
		assertEquals(0, tileSet.getDiscardedTiles().size());
	}

	@Test
	public void giveTiles() {
		final TileSets tileSet = new TileSets();
		assertEquals(14, tileSet.giveTilesFromWalls(14).size());
		assertEquals(109, tileSet.getWall().size());
		assertEquals(13, tileSet.getDeadWall().size());
		assertEquals(0, tileSet.getDiscardedTiles().size());
	}

	@Test
	public void giveTileFromDeadWall() {
		final TileSets tileSet = new TileSets();
		final ITile tile = tileSet.giveTileFromDeadWall();
		assertFalse(tileSet.getDeadWall().contains(tile));
	}

	@Test
	public void lastDiscarded() {
		final TileSets tileSet = new TileSets();
		final ITile last = TestConstructHelper.tile("N3");
		tileSet.getDiscardedTiles().add(last);
		assertEquals(last, tileSet.lastDiscarded());
	}

	@Test
	public void lastDiscardedNull() {
		final TileSets tileSet = new TileSets();
		assertNull(tileSet.lastDiscarded());
	}
}
