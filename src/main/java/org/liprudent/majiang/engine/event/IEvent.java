package org.liprudent.majiang.engine.event;

import java.io.Serializable;

import org.liprudent.majiang.engine.player.IPlayer.Wind;

public interface IEvent extends Serializable {

    /**
     * @return The players name concerned by this event
     */
    String getName();

    /**
     * @return The wind of the player concerned by this event
     */
    Wind getPlayerWind();

    /**
     * @return The public view of this event. Some internal aspect of this event
     *         maybe eluded.
     */
    IEvent publicView();

    enum KindOfEvent {
        WIND,
        CONCEALED,
        CONCEALED_OTHER,
        STATE,
        DISCARDED,
        MOVED_FROM_CONCEALED,
        GAME_OPTIONS,
        MOVED_FROM_CONCEALED_TO_HIDDEN_OTHER,
        MOVE_DISCARDED_TO_OPEN,
        MOVE_TILE_IN_OPEN_KONG,
        SCORE,
        DRAW,
        GAME_OVER
    }

    KindOfEvent getKind();
}
