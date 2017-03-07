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
package org.liprudent.majiang.engine.round;

import org.liprudent.majiang.engine.player.IState;

public enum State implements IState {
	// player just received a tile from wall or dead wall
	HAVE_A_NEW_TILE,
	// player just throwed a tile or choosed to pass
	END,
	// player is waiting a tile is throwed
	WAIT_TILE_THROWED,
	// a tile has been throwed, player must do something
	CHOOSE_ACTION,
	// player has chosen to do something with the throwed tile. Now he must wait
	// other player to perform his action.
	WAIT_OTHERS_ACTION,
	// player is round winner
	ROUND_WINNER,
	// player is round looser
	ROUND_LOOSER
}
