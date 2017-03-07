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
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.liprudent.majiang.engine.tile.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Collections;

import org.junit.Test;
import org.liprudent.majiang.engine.game.impl.TestConstructHelper;
import org.liprudent.majiang.engine.tile.ITile;

/**
 * 
 * @author jerome
 */
public class TileComputerJavaTest {

	public TileComputerJavaTest() {
	}

	@Test
	public void testIsKong() {
		System.out.println("isKong");
		final Collection<ITile> kong = TestConstructHelper.kong(1);
		final boolean expResult = true;
		final boolean result = TileComputer.isKong(kong);
		assertEquals(expResult, result);
	}

	@Test
	public void testIsKong4Tiles() {
		System.out.println("isKong");
		final Collection<ITile> kong = TestConstructHelper.kong(1);
		kong.remove(kong.iterator().next());
		final boolean expResult = false;
		final boolean result = TileComputer.isKong(kong);
		assertEquals(expResult, result);
	}

	@Test
	public void isMahjong() {
		assertMahjong("1s0,2s0,3s0,4s0,5s0,6s0,7s0,8s0,9s0,1b0,2b0,3b0,4b0,4b1");
		assertMahjong("1s0,2s1,3s1,4s1,5s1,6s1,7s1,8s1,9s1,1b1,2b1,3b1,4b2,4b3");
	}

	private void assertMahjong(final String concealedHand) {
		assertTrue(TileComputer.isMahjong(TestConstructHelper
				.set(concealedHand), Collections.EMPTY_SET,
				Collections.EMPTY_SET));
	}

}