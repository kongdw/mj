package org.liprudent.majiang.engine.tile;

import java.io.Serializable;
import java.util.List;
import java.util.SortedSet;

/**
 * This interface contains all sets of tiles we have to manage during a round
 * 麻将牌集合，每局生成新的实例
 *
 */
public interface ITileSets extends Serializable {

    /**
     * The wall is a set of tiles where people get new tiles
     * 获取牌墙
     * @return A list of tiles
     */
    List<ITile> getWall();

    /**
     * The dead wall is a set of tiles where people get new tiles each time they
     * kong
     *
     * @return A list of tiles
     */
    List<ITile> getDeadWall();

    /**
     * the set of discarded tiles is where people discards tiles. It's empty
     * when round starts. A player can take the last discarded tile under
     * certain circumstances.
     *
     * @return A list of tiles
     */
    List<ITile> getDiscardedTiles();

    /**
     * 从牌墙中获取指定数目的牌
     * @param nbTiles is the number of tiles to extract from wall
     * @return 排序后的牌集合
     */
    SortedSet<ITile> giveTilesFromWalls(int nbTiles);

    /**
     *
     * @return ATile from dead wall
     */
    ITile giveTileFromDeadWall();

    /**
     * 上一玩家打出的牌
     * @return the last discarded tile, null if none
     */
    ITile lastDiscarded();

    /**
     * 删除上一玩家打出的牌
     * remove the last discarded tile
     */
    void removeLastDiscardedTile();
}
