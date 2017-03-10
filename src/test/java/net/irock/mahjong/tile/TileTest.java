package net.irock.mahjong.tile;

import org.junit.Test;

import static net.irock.mahjong.tile.Tile.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
/**
 * 测试麻将牌
 * @author David Kong
 * @date 2017/3/10
 */
public class TileTest {

    @Test
    public void testCharacter(){
        assertEquals(1,C1.getNumber());
        assertEquals(9,C9.getNumber());
        assertTrue(C1.getNumber() < C2.getNumber());
        assertTrue(C5.getType() == C2.getType());
        assertTrue(C3.getType() == TileType.CHARACTER);
        assertEquals(C1,valueOf(0));
        assertTrue(C1.isCarryOne());
        assertTrue(C9.isCarryOne());
    }

    @Test
    public void testBamboo(){
        assertEquals(1,B1.getNumber());
        assertEquals(9,B9.getNumber());
        assertTrue(B1.getNumber() < B2.getNumber());
        assertTrue(B5.getType() == B6.getType());
        assertTrue(B6.getType() != C6.getType());
        assertTrue(B3.getType() == TileType.BAMBOO);
        assertEquals(B1,valueOf(18));
        assertTrue(B1.isCarryOne());
        assertTrue(B9.isCarryOne());
    }

    @Test
    public void testDot(){
        assertEquals(1,D1.getNumber());
        assertEquals(9,D9.getNumber());
        assertTrue(D1.getNumber() < D2.getNumber());
        assertTrue(D5.getType() == D6.getType());
        assertTrue(D6.getType() != C6.getType());
        assertTrue(D3.getType() == TileType.DOT);
        assertEquals(D1,valueOf(9));
        assertTrue(D1.isCarryOne());
        assertTrue(D9.isCarryOne());
    }

    @Test
    public void testWind(){
        final int tileIndex = 27;
        assertEquals(0,EAST.getNumber());
        assertTrue(EAST.getNumber() == NORTH.getNumber());
        assertTrue(EAST.getType() == NORTH.getType());
        assertEquals(tileIndex,EAST.getCode());
    }
}
