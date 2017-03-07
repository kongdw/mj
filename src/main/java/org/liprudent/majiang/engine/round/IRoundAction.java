package org.liprudent.majiang.engine.round;

import java.io.Serializable;
import java.util.Collection;

import org.liprudent.majiang.engine.player.IPlayer;
import org.liprudent.majiang.engine.tile.ITile;

/**
 * This interface contains signature of methods designed to be called by clients
 * in order to interact with the game.
 * 
 * @author jerome
 * 
 * 
 */
public interface IRoundAction extends Serializable{

	/**
	 * @param player
	 *            Player doing an action
	 * @param action
	 *            the Action
	 * @return true if this action is valid
	 */
	boolean handlePlayer(final IPlayer player, final Action action, ITile tile);

	/**
	 * @param player
	 * @param action
	 * @param tiles
	 * @return  true if this action is valid
	 */
	boolean handlePlayer(IPlayer player, Action action, Collection<ITile> tiles);

	/**
	 * @param player
	 * @param action
	 * @return  true if this action is valid
	 */
	boolean handlePlayer(IPlayer player, Action action);

}