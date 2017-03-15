package org.liprudent.majiang.engine.tile.impl;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author David Kong
 * @date 2017/3/14
 */
public class TileSuitBambooTest extends TestCase {

    private List<TileSuitBamboo> sortedTiles;

    @Override
    protected void setUp() throws Exception {
        sortedTiles = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            sortedTiles.add(new TileSuitBamboo(i, i + 1));
        }
    }

    public void testCompareTo() {
        List<TileSuitBamboo> tiles = new ArrayList<>();
        for (int i = 2; i >= 0; i--) {
            tiles.add(new TileSuitBamboo(i,i+1));
        }
        Collections.sort(tiles);
        for (int i = 0; i < 3; i++) {
            assertTrue(tiles.get(i).compare(sortedTiles.get(i)));
        }
    }

}
