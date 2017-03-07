package org.liprudent.majiang.engine.game;

import java.io.Serializable;
import java.util.Collection;

import org.liprudent.majiang.engine.round.Action;
import org.liprudent.majiang.engine.tile.ITile;

/**
 * This interface contains signature of methods designed to be called by clients
 * in order to interact with the game.
 *
 * @author jerome
 */
public interface IGameAction extends Serializable {

    /**
     * @param player Player doing an action
     * @param action the Action
     * @return true if the action is valid or if it's invalid but the action had
     * been registered (for instance player tried to pung and it's
     * invalid but the action has been registered so that this player's
     * state is set to END). The only case it returns false is when
     * treatmnt is invalid and player state is HAVE_A_NEW_TILE
     */
    boolean handlePlayer(final String player, final Action action, final ITile tile);

    /**
     * @param player
     * @param action
     * @param tiles  <ul>
     *               <li>if action is CHOW, the tiles must be the two tiles
     *               which make the suit with the discarded (length(tiles) == 2)</li>
     *               <li>if action is EXPOSED_KONG, tiles must be
     *               the full kong (that is to say 4 tiles)</li>
     *               </ul>
     * @return true if the action is valid or if it's invalid but the action had
     * been registered (for instance player tried to pung and it's
     * invalid but the action has been registered so that this player's
     * state is set to END). The only case it returns false is when
     * treatmnt is invalid and player state is HAVE_A_NEW_TILE
     */
    boolean handlePlayer(final String player, final Action action, final Collection<ITile> tiles);

    /**
     * @param player
     * @param action
     * @return true if the action is valid or if it's invalid but the action had
     * been registered (for instance player tried to pung and it's
     * invalid but the action has been registered so that this player's
     * state is set to END). The only case it returns false is when
     * treatmnt is invalid and player state is HAVE_A_NEW_TILE
     */
    boolean handlePlayer(final String player, final Action action);

}