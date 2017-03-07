package org.liprudent.majiang.engine.tile.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.liprudent.majiang.engine.tile.ITile;
import org.liprudent.majiang.engine.tile.ITileSets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TileSets implements ITileSets {
    private final Logger log = LoggerFactory.getLogger(TileSets.class);

    /**
     * 所有牌集合
     */
    public final static Set<ITile> all;
    /**
     * 牌墙大小
     */
    public static final int WALL_SIZE = 123;
    protected List<ITile> wall;
    protected List<ITile> deadWall;
    /**
     * 已打出牌
     */
    private final List<ITile> discardedTiles;

    /*
     * 初始化麻将牌
     */
    static {
        final Set<ITile> tiles = new HashSet<ITile>();
        for (int id = 0; id < 4; id++) {
            tiles.add(new TileHonorEast(id));
            tiles.add(new TileHonorWest(id));
            tiles.add(new TileHonorNorth(id));
            tiles.add(new TileHonorSouth(id));
            tiles.add(new TileHonorRed(id));
            tiles.add(new TileHonorGreen(id));
            tiles.add(new TileHonorWhite(id));
        }
        for (int value = 1; value < 10; value++) {
            for (int id = 0; id < 4; id++) {
                tiles.add(new TileSuitBamboo(id, value));
                tiles.add(new TileSuitCharacter(id, value));
                tiles.add(new TileSuitDot(id, value));
            }
        }

        all = Collections.unmodifiableSet(tiles);
    }

    public TileSets() {
        // construct wall and dead wall
        constructWalls();
        discardedTiles = new ArrayList<ITile>();
    }

    protected void constructWalls() {
        wall = new ArrayList<ITile>();
        deadWall = new ArrayList<ITile>();
        int countWallSize = 0;
        for (final ITile tile : TileSets.all) {
            if (countWallSize < TileSets.WALL_SIZE) {
                wall.add(tile);
            } else {
                deadWall.add(tile);
            }
            countWallSize++;
        }
        Collections.shuffle(wall);
        Collections.shuffle(deadWall);
    }

    static Set<ITile> getAll() {
        return TileSets.all;
    }

    public List<ITile> getWall() {
        return wall;
    }

    public List<ITile> getDiscardedTiles() {
        return discardedTiles;
    }

    public List<ITile> getDeadWall() {
        return deadWall;
    }

    public SortedSet<ITile> giveTilesFromWalls(final int nbTiles) {
        if (nbTiles > wall.size()) {
            throw new IllegalArgumentException("There is not enough tiles");
        }
        final SortedSet<ITile> tiles = new TreeSet<ITile>(wall.subList(0, nbTiles));
        wall.removeAll(tiles);
        return tiles;
    }

    @Override
    public ITile giveTileFromDeadWall() {
        return deadWall.remove(0);
    }

    @Override
    public ITile lastDiscarded() {
        if (discardedTiles != null && !discardedTiles.isEmpty()) {
            return discardedTiles.get(discardedTiles.size() - 1);
        }
        return null;
    }

    @Override
    public void removeLastDiscardedTile() {
        discardedTiles.remove(lastDiscarded());
    }
}
