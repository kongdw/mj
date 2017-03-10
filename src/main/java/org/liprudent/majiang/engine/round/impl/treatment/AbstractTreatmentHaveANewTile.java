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

import java.util.HashMap;
import java.util.Map;

import org.liprudent.majiang.engine.player.IPlayer;
import org.liprudent.majiang.engine.round.IRound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.liprudent.majiang.engine.round.State.*;

public abstract class AbstractTreatmentHaveANewTile extends AbstractTreatment
		implements ITreatmentHaveANewTile {

	private final Logger log = LoggerFactory.getLogger(AbstractTreatmentHaveANewTile.class);
	protected final IPlayer current;

	protected AbstractTreatmentHaveANewTile(final IPlayer player,
			final IRound round) {
		super(player, round);
		current = round.getCurrentPlayer();
	}

	@Override
	public final boolean valid() {
		final boolean isCurrent = current == player;
		final boolean currentPlayerStateValid = player.getState().equals(HAVE_A_NEW_TILE);
		// Check other players state
		final boolean otherPlayersStateValid = othersAreWaitTileThrowed();
		final boolean valid = isCurrent && currentPlayerStateValid && otherPlayersStateValid;
		if(!valid){
			Map<String,Boolean> toLog = new HashMap<String,Boolean>(2);
			toLog.put("isCurrent", isCurrent);
			toLog.put("currentPlayerStateValid", currentPlayerStateValid);
			toLog.put("otherPlayersStateValid", otherPlayersStateValid);
			log.info("valid = "+ toLog.toString());
		}
		return valid && specificValid();
	}

	protected boolean othersAreWaitTileThrowed() {
		for (IPlayer player : round.getOtherPlayers()) {
			if (player.getState() != WAIT_TILE_THROWED) {
				return false;
			}
		}
		return true;
	}

	protected abstract boolean specificValid();

}
