package org.liprudent.majiang.engine.tile.impl;

import junit.framework.TestCase;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.liprudent.majiang.engine.tile.ITile;

import java.util.*;

/**
 * @author David Kong
 * @date 2017/3/14
 */
public class TileSorterJavaTest extends TestCase {

    private SortedSet<ITile> kong;
    private SortedSet<ITile> triplet;
    private SortedSet<ITile> pair;
    private SortedSet<ITile> empty;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        empty = new TreeSet<>();
        kong = new TreeSet<>();
        for (int i = 0; i < 4; i++) {
            kong.add(new TileHonorEast(i));
            kong.add(new TileSuitBamboo(i, 1));
        }
        kong.add(new TileSuitBamboo(0, 2));

        triplet = new TreeSet<>();
        for (int i = 0; i < 3; i++) {
            triplet.add(new TileHonorGreen(i));
            triplet.add(new TileSuitDot(i, 1));
        }
        triplet.add(new TileSuitDot(0, 2));

        pair = new TreeSet<>();
        for (int i = 0; i < 2; i++) {
            pair.add(new TileHonorNorth(i));
            pair.add(new TileSuitCharacter(i, 2));
        }
        pair.add(new TileSuitCharacter(0, 3));
    }

    public void testFindKong() {
        List<SortedSet<ITile>> kongs = TileSorter.findKong(kong);
        assertTrue(kongs.size() == 2);
        for (SortedSet<ITile> kong : kongs) {
            assertTrue(TileComputer.isKong(kong));
        }
        kongs = TileSorter.findKong(empty);
        assertTrue(kongs.isEmpty());
    }

    public void testFindTriplet() {
        List<SortedSet<ITile>> triplets = TileSorter.findTriplet(triplet);
        assertTrue(triplets.size() == 2);
        for (SortedSet<ITile> triplet : triplets) {
            assertTrue(TileComputer.isTriplet(triplet));
        }
        triplets = TileSorter.findTriplet(empty);
        assertTrue(triplets.isEmpty());
    }

    public void testFindPair() {
        List<SortedSet<ITile>> pairs = TileSorter.findPair(pair);
        assertTrue(pairs.size() == 2);
        for (SortedSet<ITile> pair : pairs) {
            assertTrue(TileComputer.isPair(pair));
        }
        pairs = TileSorter.findPair(empty);
        assertTrue(pairs.isEmpty());
    }

    public void testFindSequence() {
        SortedSet<ITile> hand = new TreeSet<>();
        hand.add(new TileSuitBamboo(0, 1));
        hand.add(new TileSuitBamboo(1, 1));
        hand.add(new TileSuitBamboo(0, 2));
        hand.add(new TileSuitBamboo(0, 3));
        hand.add(new TileSuitBamboo(1, 3));

        List<Collection<ITile>> expectSequences = new ArrayList<>();

        SortedSet<ITile> expectSequence1 = new TreeSet<>();
        expectSequence1.add(new TileSuitBamboo(0, 1));
        expectSequence1.add(new TileSuitBamboo(0, 2));
        expectSequence1.add(new TileSuitBamboo(0, 3));
        SortedSet<ITile> expectSequence2 = new TreeSet<>();
        expectSequence2.add(new TileSuitBamboo(1, 1));
        expectSequence2.add(new TileSuitBamboo(0, 2));
        expectSequence2.add(new TileSuitBamboo(0, 3));
        SortedSet<ITile> expectSequence3 = new TreeSet<>();
        expectSequence3.add(new TileSuitBamboo(0, 1));
        expectSequence3.add(new TileSuitBamboo(0, 2));
        expectSequence3.add(new TileSuitBamboo(1, 3));
        SortedSet<ITile> expectSequence4 = new TreeSet<>();
        expectSequence4.add(new TileSuitBamboo(1, 1));
        expectSequence4.add(new TileSuitBamboo(0, 2));
        expectSequence4.add(new TileSuitBamboo(1, 3));
        expectSequences.add(expectSequence1);
        expectSequences.add(expectSequence2);
        expectSequences.add(expectSequence3);
        expectSequences.add(expectSequence4);

        List<Collection<ITile>> sequences = TileSorter.findSequence(hand);
        System.out.println(expectSequences.equals(sequences));
        for (final Collection<ITile> sequence : sequences) {

            assertTrue(equals(sequence,(Collection<ITile>)CollectionUtils.find(expectSequences, new Predicate() {
                @Override
                public boolean evaluate(Object o) {
                    SortedSet<ITile> tiles = (SortedSet<ITile>)o;
                    return sequence.containsAll(tiles);
                }
            })));
        }

    }
    public static <T> boolean equals(Collection<T> a,Collection<T> b){
        if(a == null){
            return false;
        }
        if(b == null){
            return false;
        }
        if(a.isEmpty() && b.isEmpty()){
            return true;
        }
        if(a.size() != b.size()){
            return false;
        }
        List<T> alist = new ArrayList<T>(a);
        List<T> blist = new ArrayList<T>(b);
        Collections.sort(alist,new Comparator<T>() {
            public int compare(T o1, T o2) {
                return o1.hashCode() - o2.hashCode();
            }

        });

        Collections.sort(blist,new Comparator<T>() {
            public int compare(T o1, T o2) {
                return o1.hashCode() - o2.hashCode();
            }

        });

        return alist.equals(blist);

    }
    public void testConstructAllCombis() {
        List<SortedSet<ITile>> list = new ArrayList<>();
        SortedSet<ITile> testCombis;
        for (int i = 0; i < 4; i++) {
            testCombis = new TreeSet<>();
            testCombis.add(new TileHonorEast(i));
            list.add(testCombis);
        }
        Collection<SortedSet<ITile>> combis = TileSorter.constructAllCombis(list, 3);
        assertNotNull(combis);
        assertEquals(4, combis.size());
    }

}
