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

import org.liprudent.majiang.engine.player.IPlayer;
import org.liprudent.majiang.engine.round.IRound;

public abstract class AbstractTreatment implements ITreatment {
	protected final IPlayer player;
	protected final IRound round;

	protected AbstractTreatment(final IPlayer player, final IRound round) {
		if (round == null) {
			throw new IllegalArgumentException("round is null");
		}

		if (player == null) {
			throw new IllegalArgumentException("player is null");
		}

		this.player = player;
		this.round = round;
	}

	@Override
	public IPlayer getPlayer() {
		return player;
	}

	@Override
	public String toString() {
		return this.getClass() + " [player=" + player + "]";
	}
	
	

}
