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

import groovy.util.GroovyTestCase
import java.util.Collections
import org.liprudent.majiang.engine.tile.impl.TileSorter;
import org.liprudent.majiang.engine.tile.impl.TileHonorEast;
import org.liprudent.majiang.engine.tile.impl.TileSuitBamboo;

class TileSuitBambooTest extends GroovyTestCase {

    void testCompareTo() {
       def hand = []
        [2,1,0].each{
            hand << new TileSuitBamboo(it, it+1) 
        }
        Collections.sort(hand)
        [0,1,2].each{
            assert hand[it] == new TileSuitBamboo(it, it+1) 
        }
    }
}
