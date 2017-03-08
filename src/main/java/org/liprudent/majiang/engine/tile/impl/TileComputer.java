package org.liprudent.majiang.engine.tile.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import org.apache.commons.collections.CollectionUtils;
import org.liprudent.majiang.engine.tile.ITile;
import org.liprudent.majiang.engine.tile.ITileSuit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class intends to perform various computation on tiles
 *
 * @author David Kong
 */
abstract public class TileComputer {

    private static Logger LOG = LoggerFactory.getLogger(TileComputer.class);

    static Collection<Collection<ITile>> findMahjong(final SortedSet<ITile> hand, final int mustFindInHand) {
        final Collection<Collection<ITile>> allCombis = TileComputer.findAllCombis(hand);
        LOG.debug("allCombis:" + allCombis.size());
        return TileComputer.mahjong(allCombis, mustFindInHand, new HashSet<Collection<ITile>>());
    }

    /**
     * @param hand
     * @return all Kong, Pung, Chow
     */
    private static Collection<Collection<ITile>> findAllCombis(final SortedSet<ITile> hand) {
        final Collection<Collection<ITile>> ret = new HashSet<Collection<ITile>>();
        ret.addAll(TileSorter.findKong(hand));
        ret.addAll(TileSorter.findPong(hand));
        ret.addAll(TileSorter.findChow(hand));
        ret.addAll(TileSorter.findPair(hand));
        return ret;
    }

    /**
     * This is a recursive method to find if we have a mahjong TODO optimize
     *
     * @param allCombis      all combinasions possible
     * @param mustFindInHand number of chow, kong or pung to find
     * @param choosedCombis  A collection of combinasions that make the mahjong possible
     * @return
     */
    private static Collection<Collection<ITile>> mahjong(final Collection<Collection<ITile>> allCombis,
                                                         final int mustFindInHand,
                                                         final Collection<Collection<ITile>> choosedCombis) {

        TileComputer.LOG.debug("allCombis:" + allCombis);
        TileComputer.LOG.debug("must find in hand :" + mustFindInHand);
        TileComputer.LOG.debug("choosed combis" + choosedCombis);

        // We have a mahjong in this case:
        if (mustFindInHand == 0 && allCombis.size() == 1
                && TileComputer.isPair(allCombis.iterator().next())) {
            choosedCombis.add(allCombis.iterator().next());
            TileComputer.LOG.debug("!!! this is mahjong:" + choosedCombis);
            return choosedCombis;
        } else if (mustFindInHand == 0) {
            return null;
        }

        final Collection<Collection<ITile>> clonedAllCombis = TileComputer.cloneAllCombis(allCombis);
        final Collection<Collection<ITile>> clonedChoosedCombis = TileComputer.cloneAllCombis(choosedCombis);
        for (final Collection<ITile> combi : clonedAllCombis) {
            if (!TileComputer.isPair(combi)) {
                TileComputer.LOG.debug("current combi = " + combi);
                final Collection<Collection<ITile>> newAllCombis = TileComputer.deleteIntersections(combi, clonedAllCombis);
                TileComputer.LOG.debug("new all combis = " + newAllCombis);
                clonedChoosedCombis.add(combi);
                newAllCombis.remove(combi);
                // recursive call to itself
                final Collection<Collection<ITile>> winningHand = TileComputer.mahjong(newAllCombis, mustFindInHand - 1, clonedChoosedCombis);
                if (winningHand != null) {
                    return winningHand;
                }
            }
        }

        return null;
    }

    static Collection<Collection<ITile>> cloneAllCombis(final Collection<Collection<ITile>> allCombis) {
        final Set<Collection<ITile>> ret = new HashSet<Collection<ITile>>(allCombis.size());
        for (final Collection<ITile> coll : allCombis) {
            ret.add(coll);
        }
        return ret;
    }

    /**
     * @param combi
     * @param combis
     * @return a collection where none element of combis contain combi. For
     * instance with combi = [1,2] and combis = [[1,3],[2,3],[3,4]]
     * result is [[3,4]]
     */
    static Collection<Collection<ITile>> deleteIntersections(final Collection<ITile> combi,
                                                             final Collection<Collection<ITile>> combis) {
        final Set<Collection<ITile>> ret = new HashSet<Collection<ITile>>();
        for (final Collection<ITile> col : combis) {
            if (!CollectionUtils.containsAny(col, combi)) {
                ret.add(col);
            }
        }
        return ret;
    }

    private TileComputer() {
    }

    /**
     * @param kong Collection of tile
     * @return true if kong is really a kong, false otherwise
     */
    public static final boolean isKong(final Collection<ITile> kong) {
        return TileComputer.isSimilar(kong, 4);
    }

    /**
     * @param pong Collection of tile
     * @return true if kong is really a kong, false otherwise
     */
    public static final boolean isPong(final Collection<ITile> pong) {
        return TileComputer.isSimilar(pong, 3);
    }

    public static final boolean isChow(final Collection<ITile> chow) {
        return TileComputer.isSuit(chow, 3);
    }

    public static final boolean isPair(final Collection<ITile> pair) {
        return TileComputer.isSimilar(pair, 2);
    }

    /**
     * @param hand
     * @param openedHand
     * @param hiddenHand
     * @return true if mahjong is really a mahjong
     */
    public static boolean isMahjong(final SortedSet<ITile> hand,
                                    final Collection<SortedSet<ITile>> openedHand,
                                    final Collection<SortedSet<ITile>> hiddenHand) {
        // count the number of chow, pong, kong
        final int count = hiddenHand.size() + openedHand.size();
        // A mahjong is 4 kong,pong,chow + a pair
        // mustFindInHand is the number of kong,pung,chow we must find in hand
        final int mustFindInHand = 4 - count;
        TileComputer.LOG.debug("Must find in hand : " + mustFindInHand);
        if (hand.size() < (mustFindInHand * 3) + 2) {
            return false;
        }
        return TileComputer.findMahjong(hand, mustFindInHand) != null;
    }

    /**
     * @param hand
     * @param find
     * @param nbToFind
     * @return true if there are at least "nbToFind" tiles "find" in "hand"
     */
    public static final boolean haveIn(final Collection<ITile> hand,
                                       final Collection<ITile> find, final int nbToFind) {
        if (nbToFind < 0) {
            throw new IllegalArgumentException("Must find at least 0 tile");
        }

        return CollectionUtils.intersection(hand, find).size() >= nbToFind;
    }

    static final boolean isSimilar(final Collection<ITile> tiles, final int nbSimilar) {
        boolean isSimilar = tiles != null && tiles.size() == nbSimilar;
        if (!isSimilar) {
            return false;
        }
        final Iterator<ITile> iterator = tiles.iterator();
        final ITile firstTile = iterator.next();
        while (iterator.hasNext() && isSimilar) {
            isSimilar &= firstTile.compare(iterator.next());
        }
        return isSimilar;
    }

    /**
     * This method seems complex: TODO refactoring: use treeset?
     */
    private static final boolean isSuit(final Collection<ITile> tiles,
                                        final int suitLength) {
        final boolean isSuit = tiles != null && tiles.size() == suitLength;
        if (!isSuit) {
            return false;
        }

        final Iterator<ITile> iterator = tiles.iterator();
        final ITile firstTile = iterator.next();

        // concept of suit only exists with TileSuit tiles
        if (!(firstTile instanceof ITileSuit)) {
            return false;
        }

        final Class clazz = firstTile.getClass();
        final List<AbstractTileSuit> temp = new ArrayList<AbstractTileSuit>();
        temp.add((AbstractTileSuit) firstTile);

        while (iterator.hasNext()) {
            final ITile next = iterator.next();
            if (clazz.isInstance(next)) {
                temp.add((AbstractTileSuit) next);
            } else {
                return false;
            }
        }
        // check ordering
        Collections.sort(temp);
        final Iterator<AbstractTileSuit> itTemp = temp.iterator();
        AbstractTileSuit prev = itTemp.next();
        while (itTemp.hasNext()) {
            final AbstractTileSuit next = itTemp.next();
            if (next.getValue() - 1 != prev.getValue()) {
                return false;
            } else {
                prev = next;
            }
        }

        return true;
    }

}
