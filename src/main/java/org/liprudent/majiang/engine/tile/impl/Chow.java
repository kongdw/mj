/**
 * Majiang is a library that implements Mahjong game rules.
 *
 * Copyright 2009 Prudent Jérome
 *
 *     This file is part of Majiang.
 *
 *     Majiang is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Majiang is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 *
 *
 * You can contact me at jprudent@gmail.com
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.liprudent.majiang.engine.tile.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import org.liprudent.majiang.engine.tile.ITile;
import org.liprudent.majiang.engine.tile.ITileSuit;

/**
 * 吃牌
 * @author David Kong
 */
public class Chow {

    private ITileSuit start;
    private Collection<ITileSuit> col2 = new ArrayList<ITileSuit>(4);
    private Collection<ITileSuit> col3 = new ArrayList<ITileSuit>(4);

    Chow(ITileSuit startTile) {
        start = startTile;
    }

    void add(ITileSuit tile) {
        int gap = tile.getValue() - start.getValue();
        if (!tile.getClass().isInstance(start) || gap == 0) {
            return;
        }
        if (gap == 1) {
            col2.add(tile);
        } else if (gap == 2) {
            col3.add(tile);
        }
    }

    boolean isChow() {
        return start != null && col2.size() > 0 && col3.size() > 0;
    }

    Collection<Collection<ITile>> expand() {
        Collection<Collection<ITile>> ret = new HashSet<Collection<ITile>>();
        for (ITile t2 : col2) {
            for (ITile t3 : col3) {
                ret.add(Arrays.asList((ITile) start, t2, t3));
            }
        }
        return ret;
    }
}
