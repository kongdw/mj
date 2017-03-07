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

import java.util.SortedSet;

import org.liprudent.majiang.engine.player.EatAction;
import org.liprudent.majiang.engine.player.IPlayer;
import org.liprudent.majiang.engine.round.IRound;
import org.liprudent.majiang.engine.round.Scoring;
import org.liprudent.majiang.engine.round.State;
import org.liprudent.majiang.engine.round.impl.ScoringMcr;
import org.liprudent.majiang.engine.tile.ITile;

import scala.collection.JavaConversions;

public class TreatmentChooseActionMahjong extends
		AbstractTreatmentChooseActionEat implements IEatAction, IMahjongAction {
	final ITile lastDiscarded;
	private Scoring scoring; 

	protected TreatmentChooseActionMahjong(final IPlayer player,
			final IRound round) {
		super(player, round);
		lastDiscarded = round.getTileSet().lastDiscarded();
	}

	@Override
	public void changeStates() {
		player.setState(State.WAIT_OTHERS_ACTION);
	}

	@Override
	public void doTreatment() {
	}

	@Override
	public boolean specificValid() {
		final ScoringMcr scoring = new ScoringMcr(JavaConversions
				.asIterable(round.getVisibleTiles()), player.getTiles());
		final boolean isMahjong = scoring.isMahjong();
		if(isMahjong) this.scoring = scoring;
		return isMahjong;
	}

	@Override
	public EatAction getEatAction() {
		return EatAction.MAHJONG;
	}

	@Override
	protected SortedSet<ITile> ownedTiles() {
		return player.getTiles().getConcealedHand();
	}

	@Override
	public boolean immediateEnd() {
		return false;
	}

	@Override
	public Scoring getScoring() {
		return scoring;
	}
}
