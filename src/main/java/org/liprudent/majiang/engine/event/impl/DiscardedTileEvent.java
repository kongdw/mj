package org.liprudent.majiang.engine.event.impl;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.liprudent.majiang.engine.event.IDiscardedTile;
import org.liprudent.majiang.engine.event.IEvent;
import org.liprudent.majiang.engine.player.IPlayer.Wind;
import org.liprudent.majiang.engine.tile.ITile;

public class DiscardedTileEvent extends BasicEvent implements IDiscardedTile {

    final ITile tile;
    final KindOfEvent kind = KindOfEvent.DISCARDED;

    public DiscardedTileEvent(final String name, final Wind wind, final ITile tile) {
        super(name, wind);
        this.tile = tile;
    }

    @Override
    public ITile getTile() {
        return tile;
    }

    @Override
    public IEvent publicView() {
        // this event can be viewed by anybody
        return this;
    }

    @Override
    public String toString() {
        return super.toString() + ToStringBuilder.reflectionToString(this);
    }

    @Override
    public KindOfEvent getKind() {
        return kind;
    }
}
