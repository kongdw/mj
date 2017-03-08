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
import org.liprudent.majiang.engine.tile.impl.TileSorter;
import org.liprudent.majiang.engine.tile.impl.TileHonorEast;
import org.liprudent.majiang.engine.tile.impl.TileSuitBamboo;

class TileSorterTest extends GroovyTestCase {
    void testFindKong() {
        def hand = []
        [0,1,2,3].each{
            hand << new TileHonorEast(it) << new TileSuitBamboo(it,1)
        }
        hand << new TileSuitBamboo(0,2)
        assert hand.size() == 9

        def kongs = TileSorter.findKong(hand)
        assert kongs.size() == 2
        kongs.each{
            assert TileComputer.isKong(it)
        }
    }

    void testFindKongEmpty() {
        def hand = []
        def kongs = TileSorter.findKong(hand)
        assert kongs.size() == 0
    }

    void testFindPung() {
        def hand = []
        [0,1,2].each{
            hand << new TileHonorEast(it) << new TileSuitBamboo(it,1)
        }
        hand << new TileSuitBamboo(0,2) << new TileSuitBamboo(0,3)
        assert hand.size() == 8 

        def pungs = TileSorter.findPung(hand)
        assert pungs.size() == 2
        pungs.each{
            assert TileComputer.isPung(it)
        }
    }

    void testConstructAllCombis(){
        def hand = []
        [0,1,2,3].each{
            hand << ([] << new TileHonorEast(it))
        }

        def res = TileSorter.constructAllCombis(hand,3)
        assert res.size() == 4 : "It's " + res.size() + " res=" +res
    }

    void testFindPungClone() {
        def hand = []
        [0,1,2,3].each{
            hand << new TileHonorEast(it)
        }

        def pungs = TileSorter.findPung(hand)
        assert pungs.size() == 4 : "It's " + pungs.size()
        pungs.each{
            assert TileComputer.isPung(it)
        }
    }


    void testFindPair() {
        def hand = []
        [0,1,2].each{
            hand << new TileSuitBamboo(it,1)
        }

        def pairs = TileSorter.findPair(hand)
        assert (pairs as Set).size() == 3 
        pairs.each{
            assert (it as Set).size() == 2
        }
    }

    void testFindPungEmpty() {
        def hand = []
        def pungs = TileSorter.findPung(hand)
        assert pungs.size() == 0
    }

    void testFindChow(){
        def hand = [] << new TileSuitBamboo(0,1) << new TileSuitBamboo(1,1) << new TileSuitBamboo(0,2) << new TileSuitBamboo(0,3) << new TileSuitBamboo(1,3)
        def mustBe = []
        mustBe << ([] << new TileSuitBamboo(0,1) << new TileSuitBamboo(0,2) << new TileSuitBamboo(0,3))
        mustBe << ([] << new TileSuitBamboo(1,1) << new TileSuitBamboo(0,2) << new TileSuitBamboo(0,3))
        mustBe << ([] << new TileSuitBamboo(0,1) << new TileSuitBamboo(0,2) << new TileSuitBamboo(1,3))
        mustBe << ([] << new TileSuitBamboo(1,1) << new TileSuitBamboo(0,2) << new TileSuitBamboo(1,3))
        assert mustBe.size() == 4

        def chows = TileSorter.findChow(hand)
        assert chows.size() == 4
       // assert mustBe[0] as Set ==  ([] << new TileSuitBamboo(0,1) << new TileSuitBamboo(0,3) << new TileSuitBamboo(0,2)) as Set
        chows.each{ chow ->
            assert mustBe.find{must ->
                must as Set == chow as Set
            }
        }
    }
}