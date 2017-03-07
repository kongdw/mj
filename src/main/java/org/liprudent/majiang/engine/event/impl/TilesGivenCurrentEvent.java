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

import java.util.SortedSet;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.liprudent.majiang.engine.event.IEvent;
import org.liprudent.majiang.engine.event.ITilesGivenEvent;
import org.liprudent.majiang.engine.event.KindOfHand;
import org.liprudent.majiang.engine.event.KindOfWall;
import org.liprudent.majiang.engine.event.IEvent.KindOfEvent;
import org.liprudent.majiang.engine.player.IPlayer.Wind;
import org.liprudent.majiang.engine.tile.ITile;


import com.google.common.base.Preconditions;

public class TilesGivenCurrentEvent extends BasicEvent implements
		ITilesGivenEvent {
	final SortedSet<ITile> tiles;
	final KindOfWall source;
	final KindOfHand destination;
	final KindOfEvent kind = KindOfEvent.CONCEALED;
	final transient IEvent publicView;

	public TilesGivenCurrentEvent(final String name, final Wind wind,
			final KindOfWall source, final KindOfHand destination,
			final SortedSet<ITile> tiles) {
		super(name, wind);
		Preconditions.checkNotNull(tiles, "Tiles can't be null");
		this.tiles = tiles;
		this.source = source;
		this.destination = destination;
		publicView = new TilesGivenPublicEvent(name, wind, source, destination,
				tiles.size());
	}

	@Override
	public SortedSet<ITile> getTiles() {
		return tiles;
	}

	@Override
	public KindOfHand getDestination() {
		return KindOfHand.CONCEALED;
	}

	@Override
	public Integer getNbTiles() {
		return tiles.size();
	}

	@Override
	public KindOfWall getSource() {
		return KindOfWall.WALL;
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public boolean equals(final Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	@Override
	public IEvent publicView() {
		return publicView;
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
