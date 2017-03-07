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
package org.liprudent.majiang.engine.round.impl.treatment;

import java.io.Serializable;

import org.liprudent.majiang.engine.player.IPlayer;

/**
 * @author jerome
 * 
 */
public interface ITreatment extends Serializable{
	/**
	 * This method is called first<br>
	 * 
	 * @return true if treatment pre conditions are satisfied, false otherwise
	 */
	boolean valid();

	/**
	 * This method is called secondly<br>
	 * Do the treatment like, give tiles, modify tile sets, ...
	 */
	void doTreatment();

	/**
	 * This method is called lastly<br>
	 * Change state of players after doTreatment done
	 */
	void changeStates();

	/**
	 * @return the player under treatment
	 */
	IPlayer getPlayer();
}
