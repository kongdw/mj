package org.liprudent.majiang.engine.tile.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class AbstractTileHonorTest {
	@Test
	public void equals(){
		TileHonorEast tileA=new TileHonorEast(0);
		TileHonorEast tileB=new TileHonorEast(1);
		assertFalse(tileA.equals(tileB));
	}
	@Test
	public void compare(){
		TileHonorEast tileA=new TileHonorEast(0);
		TileHonorEast tileB=new TileHonorEast(1);
		assertTrue(tileA.compare(tileB));
	}
}
