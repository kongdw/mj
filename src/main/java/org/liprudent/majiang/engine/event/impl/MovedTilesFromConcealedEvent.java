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

import java.util.Collection;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.liprudent.majiang.engine.event.IEvent;
import org.liprudent.majiang.engine.event.IMovedTilesFromConcealed;
import org.liprudent.majiang.engine.event.IMovedTilesFromConcealedToHiddenPublicEvent;
import org.liprudent.majiang.engine.event.KindOfHand;
import org.liprudent.majiang.engine.event.IEvent.KindOfEvent;
import org.liprudent.majiang.engine.player.IPlayer.Wind;
import org.liprudent.majiang.engine.tile.ITile;

public class MovedTilesFromConcealedEvent extends BasicEvent implements
		IMovedTilesFromConcealed {

	final private KindOfHand destination;

	final private Collection<ITile> tiles;
	final KindOfEvent kind = KindOfEvent.MOVED_FROM_CONCEALED;

	public MovedTilesFromConcealedEvent(final String name, final Wind wind,
			final KindOfHand destination, final Collection<ITile> tiles) {
		super(name, wind);
		this.destination = destination;
		this.tiles = tiles;
	}

	@Override
	public KindOfHand getDestination() {
		return destination;
	}

	@Override
	public Collection<ITile> getTiles() {
		return tiles;
	}

	class MovedTilesFromConcealedToHiddenPublicEvent extends BasicEvent
			implements IMovedTilesFromConcealedToHiddenPublicEvent {

		final KindOfEvent kind = KindOfEvent.MOVED_FROM_CONCEALED_TO_HIDDEN_OTHER;

		public MovedTilesFromConcealedToHiddenPublicEvent(String name, Wind wind) {
			super(name, wind);
		}

		@Override
		public IEvent publicView() {
			throw new IllegalStateException("public view shouln't be called");
		}

		@Override
		public Integer getNbTiles() {
			return getTiles().size();
		}

		@Override
		public KindOfEvent getKind() {
			return kind;
		}

	}

	@Override
	public IEvent publicView() {
		if (destination == KindOfHand.OPENED)
			return this;
		else if (destination == KindOfHand.HIDDEN) {
			return new MovedTilesFromConcealedToHiddenPublicEvent(getName(),
					getPlayerWind());
		}
		throw new IllegalStateException(
				"destination must be either OPENED or HIDDEN");
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
