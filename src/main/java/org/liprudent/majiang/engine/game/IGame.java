package org.liprudent.majiang.engine.game;

import java.io.Serializable;

import org.liprudent.majiang.engine.round.IRound;

public interface IGame extends Serializable {

    /**
     * @return last round of the game
     */
    IRound getLastRound();

}
