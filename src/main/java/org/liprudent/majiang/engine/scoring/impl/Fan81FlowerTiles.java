package org.liprudent.majiang.engine.scoring.impl;

import java.util.Collection;

import org.liprudent.majiang.engine.scoring.IFan;

public class Fan81FlowerTiles implements IFan {
    @Override
    public Short getNumber() {
        return 81;
    }

    @Override
    public String getName() {
        return "Flower Tiles";
    }

    @Override
    public Collection<IFan> getImplied() {
        return null;
    }
}
