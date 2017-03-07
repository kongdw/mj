package org.liprudent.majiang.engine.event.impl;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.liprudent.majiang.engine.event.IEvent;
import org.liprudent.majiang.engine.event.ITilesGivenPublicEvent;
import org.liprudent.majiang.engine.event.KindOfHand;
import org.liprudent.majiang.engine.event.KindOfWall;
import org.liprudent.majiang.engine.event.IEvent.KindOfEvent;
import org.liprudent.majiang.engine.player.IPlayer.Wind;

public class TilesGivenPublicEvent extends BasicEvent implements
		ITilesGivenPublicEvent {

	final KindOfWall source;
	final KindOfHand destination;
	IEvent publicView;
	final Integer size;
	final KindOfEvent kind = KindOfEvent.CONCEALED_OTHER;
	
	public TilesGivenPublicEvent(final String name, final Wind wind,
			final KindOfWall source, final KindOfHand destination,
			final Integer size) {
		super(name, wind);
		this.size = size;
		this.source = source;
		this.destination = destination;
	}

	@Override
	public KindOfHand getDestination() {
		return KindOfHand.CONCEALED;
	}

	@Override
	public Integer getNbTiles() {
		return size;
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
		return null;
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
