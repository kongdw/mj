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
package org.liprudent.majiang.engine.event;

import java.io.Serializable;

import org.liprudent.majiang.engine.player.IPlayer.Wind;

public interface IEvent extends Serializable{

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
		WIND,CONCEALED,CONCEALED_OTHER,STATE,DISCARDED,MOVED_FROM_CONCEALED,GAME_OPTIONS,MOVED_FROM_CONCEALED_TO_HIDDEN_OTHER,MOVE_DISCARDED_TO_OPEN,MOVE_TILE_IN_OPEN_KONG,SCORE,DRAW,GAME_OVER
	}
	
	KindOfEvent getKind();
}
