package org.liprudent.majiang.engine.game;

import org.liprudent.majiang.engine.event.IEventLogger;

/**
 * This interface should be the unique way for a client to get informations
 * about the game and to interact with the game
 *
 * @author jerome
 *
 */
public interface IGameClient extends IGameAction {
    /**
     * @param playerName
     * @return An event logger which contains events for the player specified
     */
    IEventLogger getEvents(String playerName);
}
