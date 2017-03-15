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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.liprudent.majiang.engine.game.impl.TestConstructHelper;
import org.liprudent.majiang.engine.player.EatAction;
import org.liprudent.majiang.engine.player.IPlayer;
import org.liprudent.majiang.engine.round.IRound;
import org.liprudent.majiang.engine.round.State;

public class TreatmentChooseActionSequenceTest {
	IRound round;

	IPlayer next;

	@Before
	public void init() {
		// construire un jeu
		round = TestConstructHelper.constructGame().getLastRound();
		round.getCurrentPlayer().setState(State.END);
		// avec le next qui a le 7 et le 8 de stone
		next = round.getCurrentPlayer().getNext();
		give7And8OfBamboos(next);
		// et comme par hasard le current a jeté un 9 stone
		round.getTileSet().getDiscardedTiles().add(
				TestConstructHelper.tile("9s0"));
		// et puisque ca intéresse le next, il chow
		next.setState(State.CHOOSE_ACTION);
	}

	private void give7And8OfBamboos(final IPlayer next) {
		// next.getConcealedHand().clear();
		// next.getConcealedHand().addAll(
		// TestConstructHelper
		// .set("N1,S1,W1,E1,r1,g1,w1,9c0,9c1,9c2,9c3,7s0,8s0"));

		TestConstructHelper.constructConcealedHand(next,
				"N1,S1,W1,E1,r1,g1,w1,9c0,9c1,9c2,9c3,7s0,8s0");

		assertEquals(13, next.getTiles().getConcealedHand().size());

	}

	private boolean callValid(final IRound round, final IPlayer player) {
		final TreatmentChooseActionChow treatment = treatment();
		return treatment.valid();
	}

	private TreatmentChooseActionChow treatment() {
		final TreatmentChooseActionChow treatment = new TreatmentChooseActionChow(
				next, round, TestConstructHelper.set("7s0,8s0"));
		return treatment;
	}

	@Test
	public void valid() {
		final boolean valid = callValid(round, next);
		assertTrue(valid);

	}

	@Test
	public void invalidBecauseNotNext() {

		// avec le next qui a le 7 et le 8 de stone
		final IPlayer previous = round.getCurrentPlayer().getPrevious();
		give7And8OfBamboos(previous);
		// et puisque ca intéresse le next, il chow
		previous.setState(State.CHOOSE_ACTION);
		next = previous;

		final boolean valid = callValid(round, previous);
		assertFalse(valid);

	}

	@Test
	public void invalidBecauseState() {

		next.setState(State.END);

		final boolean valid = callValid(round, next);
		assertFalse(valid);

	}

	@Test
	public void invalidBecauseNotASuit() {

		// mais le current n'a pas jeté un 9 stone (le connard)
		round.getTileSet().getDiscardedTiles().add(
				TestConstructHelper.tile("2s0"));

		final boolean valid = new TreatmentChooseActionChow(next, round,
				TestConstructHelper.set("7s0,8s0,2s0")).valid();
		assertFalse(valid);

	}

	@Test
	public void invalidBecauseNextCantMakeASuit() {
		TestConstructHelper.constructConcealedHand(next,
				"N1,S1,W1,E1,r1,g1,w1,9c0,9c1,9c2,9c3,6s0,8s0");
		assertEquals(13, next.getTiles().getConcealedHand().size());

		final boolean valid = callValid(round, next);
		assertFalse(valid);

	}

	@Test
	public void invalidBecause9StoneIsNotThrowed() {

		// mais le current n'a pas jeté un 9 stone (le connard)
		round.getTileSet().getDiscardedTiles().add(
				TestConstructHelper.tile("2s0"));

		final boolean valid = callValid(round, next);
		assertFalse(valid);

	}

	@Test
	public void changeState() {
		final TreatmentChooseActionChow trt = treatment();
		trt.changeStates();
		assertEquals(State.WAIT_OTHERS_ACTION, next.getState());
	}

	@Test
	public void getEatAction() {
		final TreatmentChooseActionChow trt = treatment();
		assertEquals(EatAction.CHOW, trt.getEatAction());
	}

	@Test
	public void figure() {
		final TreatmentChooseActionChow trt = treatment();
		assertTrue(trt.ownedTiles().containsAll(
				TestConstructHelper.set("7s0,8s0")));
	}
}
