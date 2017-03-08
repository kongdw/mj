package org.liprudent.majiang.engine.tile.impl;

import junit.framework.TestCase;
import org.liprudent.majiang.engine.tile.ITile;

import java.util.Arrays;

/**
 * @author David Kong
 * @date 2017/3/8
 */
public class TileComputerTest extends TestCase {

    /**
     * 测试杠牌
     */
    public void testIsKong() {
        ITile[] tiles = new TileHonorEast[]{
                new TileHonorEast(0),
                new TileHonorEast(1),
                new TileHonorEast(2),
                new TileHonorEast(3)
        };
        assertTrue(TileComputer.isKong(Arrays.asList(tiles)));
    }

}
