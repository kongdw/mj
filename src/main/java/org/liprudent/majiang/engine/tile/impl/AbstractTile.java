package org.liprudent.majiang.engine.tile.impl;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.liprudent.majiang.engine.tile.ITile;
import org.liprudent.majiang.engine.tile.TileFamily;

abstract public class AbstractTile implements ITile {
    private final TileFamily family;

    /**
     * id can be 0 to 3.
     */
    private final int id;

    public AbstractTile(final TileFamily family, final int id) {
        if (id < 0 || id > 3) {
            throw new IllegalArgumentException("(family=" + family + ",id=" + id + ")Id must be between 0 and 3");
        }
        this.family = family;
        this.id = id;
    }

    public TileFamily getFamily() {
        return family;
    }

    public int getId() {
        return id;
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(final Object obj) {
        return EqualsBuilder.reflectionEquals(obj, this);
    }

    @Override
    public String toString() {
        return getUniqueId();
    }

    @Override
    public String getUniqueId() {
        return String.format("%s(%d)", family.getChineseName(), id);
    }

    @Override
    public int compareTo(ITile o) {
        //consistent with equals
        if (this.equals(o)) return 0;
        else
            return getCloneValue() + getId() - o.getCloneValue() - o.getId();
    }

    @Override
    public boolean compare(final ITile obj) {
        return obj != null && (getCloneValue() - obj.getCloneValue() == 0);
    }

}
