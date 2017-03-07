package org.liprudent.majiang.engine.tile.impl;

import org.liprudent.majiang.engine.tile.ITileHonor;
import org.liprudent.majiang.engine.tile.TileFamily;

abstract public class AbstractTileHonor extends AbstractTile implements ITileHonor {


    protected AbstractTileHonor(TileFamily family, int id) {
        super(family, id);
    }

    @Override
    public int getCloneValue() {
        return (getFamily().getTileOrder() + 1) * 200;
    }

}
