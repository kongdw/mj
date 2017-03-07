package org.liprudent.majiang.engine.event;

import org.liprudent.majiang.engine.tile.ITile;

/**
 *
 */
public interface IDiscardedTile extends IEvent {
    ITile getTile();
}