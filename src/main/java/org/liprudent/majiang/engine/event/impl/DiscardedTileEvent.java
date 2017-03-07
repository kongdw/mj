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
package org.liprudent.majiang.engine.event.impl;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.liprudent.majiang.engine.event.IDiscardedTile;
import org.liprudent.majiang.engine.event.IEvent;
import org.liprudent.majiang.engine.player.IPlayer.Wind;
import org.liprudent.majiang.engine.tile.ITile;

public class DiscardedTileEvent extends BasicEvent implements IDiscardedTile {

	final ITile tile;
	final KindOfEvent kind = KindOfEvent.DISCARDED;
	
	public DiscardedTileEvent(final String name, final Wind wind,
			final ITile tile) {
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
