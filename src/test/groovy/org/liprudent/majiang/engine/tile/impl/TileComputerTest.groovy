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
package org.liprudent.majiang.engine.tile.impl

import org.liprudent.majiang.engine.tile.*;

class TileComputerTest extends GroovyTestCase {

    void testIsKong() {
       def hand = []
        [0,1,2,3].each{
            hand << new TileHonorEast(it) 
        }
        assert TileComputer.isKong(hand);
    }

    
    void testIsKong4Tiles() {
        def hand = []
        [1,2,3].each{
            hand << new TileHonorEast(it) 
        }
        assert ! TileComputer.isKong(hand);
    }
    
    void testFindPungFalse() {
        def hand = []
        [0,1,2].each{
            hand << new TileHonorEast(it) << new TileSuitBamboo(it,1)
        }
        hand << new TileSuitBamboo(0,2)
        assert ! TileComputer.isTriplet(hand)
    }
    
    void testFindPungFalse2() {
        def hand = []
        [0,1,2].each{
            hand << new TileHonorEast(it) << new TileSuitBamboo(it,1)
        }
        assert ! TileComputer.isTriplet(hand)
    }

    void testFindPungTrue() {
        def hand = []
        [0,1,2].each{
            hand << new TileHonorEast(it) 
        }
        assert TileComputer.isTriplet(hand)
    }

    void testIsSimilarFalseBecauseNull() {
        assert ! TileComputer.isSimilar(null,3)
    }

    void testSuit() {
        def hand = []
        [0,2,1].each{
            hand << new TileSuitBamboo(it,it+1) 
        }
        assert TileComputer.isSequence(hand,3)
    }

    void testSuitFalseBecauseHonor() {
        def hand = []
        [0,1,2].each{
            hand << new TileHonorEast(it) 
        }
        assert ! TileComputer.isSequence(hand,3)
    }

    void testSuitFalseBecauseNotASuit() {
        def hand = []
        [0,1,3].each{
            hand << new TileSuitBamboo(it,it+1) 
        }
        assert ! TileComputer.isSequence(hand,3)
    }

    void testSuitFalseBecauseLengthNotOk() {
        def hand = []
        [0,1,2,3].each{
            hand << new TileSuitBamboo(it,it+1) 
        }
        assert ! TileComputer.isSequence(hand,3)
    }

    void testSuitFalseBecauseTilesAreNotTheSameKind() {
        def hand = [] << new TileHonorEast(3) << new TileSuitBamboo(0,1) << new TileSuitBamboo(1,2)
        assert ! TileComputer.isSequence(hand,3)
        Collections.reverse(hand)
        assert ! TileComputer.isSequence(hand,3)
    }

    void testSuitFalseBecauseNull() {
        assert ! TileComputer.isSequence(null,3)
    }

    void testIsMahjong1() {
        def hand = [] as SortedSet
        [0,1,2].each{
            hand << new TileHonorEast(it) << new TileHonorRed(it) << new TileHonorGreen(it)
            //1,2,3 bamboo
            hand << new TileSuitBamboo(0,it+1)
        }
        [1,2].each{
            hand << new TileSuitBamboo(it,3)
        }

        def openedHand = new HashSet<Collection<ITile>>()
        def hiddenHand = new HashSet<Collection<ITile>>()
        //kong is : 1b,2b,3b,3b,3b,3*east,3*red,3*green and it's a mahjong
        assert TileComputer.isMahjong(hand,Collections.EMPTY_SET,Collections.EMPTY_SET)
    }



    void testFindMahjong3() {
        def hand = [] as SortedSet
        [0,1,2].each{
            hand << new TileSuitBamboo(it,1)<< new TileSuitBamboo(it,2)<< new TileSuitBamboo(it,3)
        }
        [1,2].each{
            hand << new TileHonorRed(it)
        }
        hand << new TileSuitBamboo(3,3)<< new TileSuitBamboo(0,4)<< new TileSuitBamboo(0,5)


        //kong is : 1b,2b,3b,1b,2b,3b,1b,2b,3b,3b,4b,5b,red*2
        Collection<ITile> mahjong = TileComputer.findMahjong(hand,4)
        assert mahjong
        def int count3 = 0
        def int count2 = 0
        mahjong.each{
            if(it.size() == 3) count3++
            else if (it.size() == 2) count2++
            else assert false : "size:"+it.size()
        }

    }

    void testIsMahjong2() {
        //extreme case
        def openedHand = [] as Collection<SortedSet>
       
        openedHand << [ [] << new TileHonorEast(0) << new TileHonorEast(1) << new TileHonorEast(2) << new TileHonorEast(3)]
        openedHand << [ [] << new TileHonorRed(0)  << new TileHonorRed(1)  << new TileHonorRed(2)  << new TileHonorRed(3)]
        openedHand << [ [] << new TileHonorSouth(0)<< new TileHonorSouth(1)<< new TileHonorSouth(2)<< new TileHonorSouth(3)]
        openedHand << [ [] << new TileHonorNorth(0)<< new TileHonorNorth(1)<< new TileHonorNorth(2)<< new TileHonorNorth(3)]

        def hand = []  << new TileHonorWest(0) << new TileHonorWest(1) as SortedSet
        assert TileComputer.isMahjong(hand,Collections.EMPTY_SET,openedHand)
    }

    void testCloneAllCombis(){
        def hand = []
        4.times{
            hand << ([] << new TileHonorEast(it))
        }
        assert hand.size() == 4 : "actual size = " + hand.size()
        def clone = TileComputer.cloneAllCombis(hand)
        assert clone.size() == 4 : "actual size is " + clone.size()
        assert clone == hand as Set
        clone <<  ([] << new TileSuitBamboo(0,2))
        assert clone.size() == 5 : "Add didn't work: size=" + clone.size()
        assert hand.size() == 4
    }


    void testDeleteIntersection(){
        def combis = []
        combis << ([] << new TileSuitBamboo(0,1) << new TileSuitBamboo(0,2) << new TileSuitBamboo(0,3))
        combis << ([] << new TileSuitBamboo(0,2) << new TileSuitBamboo(0,3) << new TileSuitBamboo(0,4))
        combis << ([] << new TileSuitBamboo(0,3) << new TileSuitBamboo(1,3))
        combis << ([] << new TileSuitBamboo(0,4) << new TileSuitBamboo(0,5) << new TileSuitBamboo(0,6))

        def combi = [] << new TileSuitBamboo(0,3) << new TileSuitBamboo(1,3)

        def deleted = TileComputer.deleteIntersections(combi,combis)
        assert deleted.size() == 1
        deleted.each{
            it == [] << new TileSuitBamboo(0,4) << new TileSuitBamboo(0,5) << new TileSuitBamboo(0,6)
        }
    }

    void testHaveIn(){
        def hand = []
        4.times{
            hand << ([] << new TileHonorEast(it))
        }

        def find = []
        3.times{
             find << ([] << new TileHonorEast(it))
        }

        assert TileComputer.haveIn(hand,find,3)
        assert TileComputer.haveIn(hand,find,2)
        assert TileComputer.haveIn(hand,find,1)
        assert TileComputer.haveIn(hand,find,0)

        find = [[new TileHonorSouth(0)]]

        assert ! TileComputer.haveIn(hand,find,1)
    }

}
