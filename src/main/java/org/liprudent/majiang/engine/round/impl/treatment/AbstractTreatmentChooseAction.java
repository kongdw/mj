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
import org.liprudent.majiang.engine.round.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract public class AbstractTreatmentChooseAction extends AbstractTreatment {

	protected Logger log = LoggerFactory.getLogger("ChooseTreatment");

	protected AbstractTreatmentChooseAction(final IPlayer player,
			final IRound round) {
		super(player, round);
	}

	@Override
	public final boolean valid() {
		final boolean isChooseAction = State.CHOOSE_ACTION == player.getState();
		if (!isChooseAction) {
			log.info("Player is invalid state. Must be CHOOSE_ACTION.");
			//TODO pass to debug
			log.info("Player info:" + player);
		}
		final boolean isCurrentEnd = State.END == round.getCurrentPlayer()
				.getState();
		if (!isCurrentEnd) {
			log.info("Current player is not in state end.");
		}

		return isChooseAction && isCurrentEnd && specificValid();
	}

	protected abstract boolean specificValid();

}
