package org.liprudent.majiang.engine.tile.impl

class TileSorterTest extends GroovyTestCase {

    void testFindChow(){
        def hand = [] << new TileSuitBamboo(0,1) << new TileSuitBamboo(1,1) << new TileSuitBamboo(0,2) << new TileSuitBamboo(0,3) << new TileSuitBamboo(1,3)
        def mustBe = []
        mustBe << ([] << new TileSuitBamboo(0,1) << new TileSuitBamboo(0,2) << new TileSuitBamboo(0,3))
        mustBe << ([] << new TileSuitBamboo(1,1) << new TileSuitBamboo(0,2) << new TileSuitBamboo(0,3))
        mustBe << ([] << new TileSuitBamboo(0,1) << new TileSuitBamboo(0,2) << new TileSuitBamboo(1,3))
        mustBe << ([] << new TileSuitBamboo(1,1) << new TileSuitBamboo(0,2) << new TileSuitBamboo(1,3))
        assert mustBe.size() == 4

        def chows = TileSorter.findSequence(hand)
        assert chows.size() == 4
       // assert mustBe[0] as Set ==  ([] << new TileSuitBamboo(0,1) << new TileSuitBamboo(0,3) << new TileSuitBamboo(0,2)) as Set
        chows.each{ chow ->
            assert mustBe.find{must ->
                must as Set == chow as Set
            }
        }
    }
}