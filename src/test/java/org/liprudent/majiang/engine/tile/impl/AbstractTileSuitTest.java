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
