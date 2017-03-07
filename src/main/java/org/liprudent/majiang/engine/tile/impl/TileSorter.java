package org.liprudent.majiang.engine.tile.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.collections.CollectionUtils;
import org.liprudent.majiang.engine.tile.ITile;
import org.liprudent.majiang.engine.tile.ITileSuit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jerome
 */
public abstract class TileSorter {

    private static Logger LOG = LoggerFactory.getLogger(TileSorter.class);

    static Collection<SortedSet<ITile>> constructAllCombis(
            final Collection<SortedSet<ITile>> list, final int sizeOfCombis) {
        if (list.size() == 0) {
            return null;
        }
        final int currentSize = list.iterator().next().size() + 1;
        final Set<SortedSet<ITile>> res = new HashSet<SortedSet<ITile>>();
        for (final Collection<ITile> c1 : list) {
            for (final Collection<ITile> c2 : list) {
                final SortedSet<ITile> merged = merge(c1, c2);
                if (merged.size() == currentSize) {
                    res.add(merged);
                }
            }
        }

        if (currentSize == sizeOfCombis) {
            return res;
        } else {
            return constructAllCombis(res, sizeOfCombis);
        }

    }

    private static SortedSet<ITile> merge(final Collection<ITile> c1, final Collection<ITile> c2) {
        return new TreeSet<ITile>(CollectionUtils.union(c1, c2));
    }

    private TileSorter() {
    }

    public static List<SortedSet<ITile>> findKong(final SortedSet<ITile> hand) {
        return findSimilar(hand, 4);
    }

    /**
     * @param hand
     * @return
     */
    public static List<SortedSet<ITile>> findPong(final SortedSet<ITile> hand) {
        LOG.debug("findPong - input:" + hand);
        List<SortedSet<ITile>> pongs = findSimilar(hand, 3);
        LOG.debug("found : " + pongs);
        return pongs;

    }

    public static List<SortedSet<ITile>> findPair(
            final SortedSet<ITile> hand) {
        return findSimilar(hand, 2);
    }

    public static List<Collection<ITile>> findChow(final Collection<ITile> hand) {
        final List<Collection<ITile>> ret = new ArrayList<Collection<ITile>>();
        // hand filtered with only suit tile
        final Collection<ITileSuit> onlyTileSuit = filter(hand);

        for (final ITileSuit tile : onlyTileSuit) {
            // this collection maybe a collection of suits :
            final Chow chow = new Chow(tile);
            for (final ITileSuit other : onlyTileSuit) {
                chow.add(other);
            }
            if (chow.isChow()) {
                ret.addAll(chow.expand());
            }
        }
        return ret;
    }

    /**
     * @param hand
     * @param nbSimilar Number of similar tiles
     * @return
     */
    static List<SortedSet<ITile>> findSimilar(final SortedSet<ITile> hand, final int nbSimilar) {

        Map<Integer, SortedSet<ITile>> handSortedByCloneValue = new HashMap<Integer, SortedSet<ITile>>(hand.size());
        for (ITile tile : hand) {
            Integer cloneValue = tile.getCloneValue();
            if (handSortedByCloneValue.get(cloneValue) == null) {
                handSortedByCloneValue.put(cloneValue, new TreeSet<ITile>());
            }
            handSortedByCloneValue.get(cloneValue).add(tile);
        }

        Map<Integer, SortedSet<ITile>> handSortedByCloneValueWithAtLeastNbSimilar = new HashMap<Integer, SortedSet<ITile>>(hand.size());
        for (Integer cloneValue : handSortedByCloneValue.keySet()) {
            if (handSortedByCloneValue.get(cloneValue).size() >= nbSimilar) {
                handSortedByCloneValueWithAtLeastNbSimilar.put(cloneValue,
                        handSortedByCloneValue.get(cloneValue));
            }
        }

        final List<SortedSet<ITile>> ret = new LinkedList<SortedSet<ITile>>();
        for (final SortedSet<ITile> sortedSet : handSortedByCloneValueWithAtLeastNbSimilar
                .values()) {
            if (sortedSet.size() == nbSimilar) {
                ret.add(sortedSet);
            } else if (sortedSet.size() > nbSimilar) {
                Collection<SortedSet<ITile>> in = new ArrayList<SortedSet<ITile>>();
                for (ITile tile : sortedSet) {
                    SortedSet<ITile> oneElement = new TreeSet<ITile>();
                    oneElement.add(tile);
                    in.add(oneElement);
                }
                ret.addAll(constructAllCombis(in, nbSimilar));
            }
        }
        return ret;
    }

    private static Collection<Collection<ITile>> listAsListofTile(final Collection<ITile> list) {
        final List<Collection<ITile>> ret = new ArrayList<Collection<ITile>>(list.size());
        for (final ITile tile : list) {
            ret.add(Arrays.asList(tile));
        }
        return ret;
    }

    /**
     * @param hand
     * @return a collection of tile which are instance of ITileSuit
     */
    private static Collection<ITileSuit> filter(final Collection<ITile> hand) {
        final Collection<ITileSuit> onlyTileSuit = new HashSet<ITileSuit>();
        for (final ITile tile : hand) {
            if (!isTileSuit(tile)) {
                continue;
            } else {
                onlyTileSuit.add((ITileSuit) tile);
            }
        }
        return onlyTileSuit;
    }

    private static boolean isTileSuit(final ITile tile) {
        return ITileSuit.class.isInstance(tile);
    }
}
