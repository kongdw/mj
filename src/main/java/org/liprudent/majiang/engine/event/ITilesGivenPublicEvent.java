package org.liprudent.majiang.engine.event;

public interface ITilesGivenPublicEvent extends IEvent {
	KindOfWall getSource();

	KindOfHand getDestination();

	Integer getNbTiles();

}
