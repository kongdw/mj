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

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.liprudent.majiang.engine.event.IEvent;
import org.liprudent.majiang.engine.event.IPlayerStateChangedEvent;
import org.liprudent.majiang.engine.event.IEvent.KindOfEvent;
import org.liprudent.majiang.engine.player.IPlayer.Wind;
import org.liprudent.majiang.engine.round.State;

public class PlayerStateChangedEvent extends BasicEvent implements
		IPlayerStateChangedEvent {

	final State state;
	final KindOfEvent kind = KindOfEvent.STATE;
	
	public PlayerStateChangedEvent(final String name, final Wind wind,
			final State state) {
		super(name, wind);
		this.state = state;
	}

	@Override
	public State getState() {
		return state;
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
