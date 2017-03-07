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

import java.util.Collection;
import java.util.SortedSet;

import org.liprudent.majiang.engine.player.EatAction;
import org.liprudent.majiang.engine.player.IPlayer;
import org.liprudent.majiang.engine.round.IRound;
import org.liprudent.majiang.engine.tile.ITile;
import org.liprudent.majiang.engine.tile.impl.TileSorter;

public class TreatmentChooseActionPung extends
		AbstractTreatmentChooseEatTheSameKind implements IEatAction {

	protected TreatmentChooseActionPung(final IPlayer player, final IRound round) {
		super(player, round);
	}

	@Override
	protected Collection<SortedSet<ITile>> findPartialFigure(
			final SortedSet<ITile> hand) {
		return TileSorter.findPair(hand);
	}

	@Override
	public EatAction getEatAction() {
		return EatAction.PONG;
	}

}
