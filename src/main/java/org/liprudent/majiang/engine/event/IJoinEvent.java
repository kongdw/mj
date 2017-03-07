package org.liprudent.majiang.engine.event;

/**
 * Event sent when a player join a game
 * @author jerome
 *
 */
public interface IJoinEvent extends IEvent{
	GameInfo getGameInfo();
}
