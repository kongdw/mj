package org.liprudent.majiang.engine.scoring.impl;

import java.util.Collection;

import org.liprudent.majiang.engine.scoring.IFan;

public class Fan80SelfDrawn implements IFan {

    @Override
    public Short getNumber() {
        return 80;
    }

    @Override
    public String getName() {
        return "Self Drawn";
    }

    @Override
    public Collection<IFan> getImplied() {
        return null;  
    }
}
