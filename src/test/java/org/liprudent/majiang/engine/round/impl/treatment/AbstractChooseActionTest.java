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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.liprudent.majiang.engine.game.impl.TestConstructHelper;
import org.liprudent.majiang.engine.player.IPlayer;
import org.liprudent.majiang.engine.round.IRound;
import org.liprudent.majiang.engine.round.State;

public class AbstractChooseActionTest {
	private static class DummyTreatmentChooseAction extends
			AbstractTreatmentChooseAction {

		protected DummyTreatmentChooseAction(final IPlayer player,
				final IRound round) {
			super(player, round);
		}

		@Override
		protected boolean specificValid() {
			return true;
		}

		@Override
		public void changeStates() {

		}

		@Override
		public void doTreatment() {

		}

	}

	IRound round;
	IPlayer player;
	DummyTreatmentChooseAction abstractChooseAction;

	@Before
	public void init() {
		round = TestConstructHelper.constructGame().getLastRound();
		player = round.getCurrentPlayer().getNext();
		player.setState(State.CHOOSE_ACTION);
		round.getCurrentPlayer().setState(State.END);
		abstractChooseAction = new DummyTreatmentChooseAction(player, round);
	}

	@Test
	public void valid() {

		assertTrue(abstractChooseAction.valid());
	}

	@Test
	public void invalidBecauseCurrentPlayerNotEnd() {

		round.getCurrentPlayer().setState(State.HAVE_A_NEW_TILE);

		assertFalse(abstractChooseAction.valid());
	}

}
