package org.liprudent.majiang.engine.tile.impl;

import org.liprudent.majiang.engine.tile.ITileSuit;
import org.liprudent.majiang.engine.tile.TileFamily;

abstract public class AbstractTileSuit extends AbstractTile implements ITileSuit {

    /**
     * value = 1 to 9
     */
    private final int value;

    protected AbstractTileSuit(final TileFamily family, final int id, final int value) {
        super(family, id);
        if (value < 1 || value > 9) {
            throw new IllegalArgumentException("Value is from 1 to 9");
        }
        this.value = value;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = super.hashCode();
        result = PRIME * result + value;
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AbstractTileSuit other = (AbstractTileSuit) obj;
        if (value != other.value) {
            return false;
        }
        return true;
    }

    @Override
    public String getUniqueId() {
        return String.format("%d%s", value, super.getUniqueId());
    }

    @Override
    public int getCloneValue() {
        return (getFamily().getTileOrder() + 1) * 300 + (getValue() + 1) * 10;
    }

    public int getValue() {
        return value;
    }

}
