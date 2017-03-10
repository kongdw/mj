package org.liprudent.majiang.engine.tile.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Collections;

import org.junit.Test;
import org.liprudent.majiang.engine.game.impl.TestConstructHelper;
import org.liprudent.majiang.engine.tile.ITile;

/**
 * @author jerome
 */
public class TileComputerJavaTest {

    public TileComputerJavaTest() {
    }

    @Test
    public void testIsKong() {
        final Collection<ITile> kong = TestConstructHelper.kong(1);
        assertTrue(TileComputer.isKong(kong));
    }

    @Test
    public void testIsKong4Tiles() {
        final Collection<ITile> kong = TestConstructHelper.kong(1);
        kong.remove(kong.iterator().next());
        assertFalse(TileComputer.isKong(kong));
    }

    @Test
    public void isMahjong() {
        assertMahjong("1s0,2s0,3s0,4s0,5s0,6s0,7s0,8s0,9s0,1b0,2b0,3b0,4b0,4b1");
//        assertMahjong("1s0,2s1,3s1,4s1,5s1,6s1,7s1,8s1,9s1,1b1,2b1,3b1,4b2,4b3");
    }

    private void assertMahjong(final String concealedHand) {
        assertTrue(TileComputer.isMahjong(TestConstructHelper.set(concealedHand), Collections.EMPTY_SET, Collections.EMPTY_SET));
    }

}