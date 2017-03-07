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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class AbstractTileSuitTest {
	@Test
	public void equals(){
		TileSuitDot tileA=new TileSuitDot(0,1);
		TileSuitDot tileB=new TileSuitDot(1,1);
		assertFalse(tileA.equals(tileB));
	}
	@Test
	public void compare(){
		TileSuitDot tileA=new TileSuitDot(0,1);
		TileSuitDot tileB=new TileSuitDot(1,1);
		assertTrue(tileA.compare(tileB));
	}
}
