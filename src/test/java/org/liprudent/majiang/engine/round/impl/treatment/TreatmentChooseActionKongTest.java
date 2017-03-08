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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.liprudent.majiang.engine.event.KindOfWall;
import org.liprudent.majiang.engine.game.impl.TestConstructHelper;
import org.liprudent.majiang.engine.player.EatAction;
import org.liprudent.majiang.engine.player.IPlayer;
import org.liprudent.majiang.engine.round.IRound;
import org.liprudent.majiang.engine.round.State;

public class TreatmentChooseActionKongTest {
	IRound round;

	IPlayer player;

	@Before
	public void init() {
		// construire un jeu
		round = TestConstructHelper.constructGame().getLastRound();
		round.getCurrentPlayer().setState(State.END);
		// avec le next qui a 3* 9 de stone
		player = round.getCurrentPlayer().getPrevious();
		giveTripleOf9Stones(player);
		// et comme par hasard le current a jeté un 9 stone
		round.getTileSet().getDiscardedTiles().add(
				TestConstructHelper.tile("9s3"));
		// et puisque ca intéresse le next, il kong
		player.setState(State.CHOOSE_ACTION);
	}

	private void giveTripleOf9Stones(final IPlayer next) {
		TestConstructHelper.clearConcealedHand(next);
		assert(next.getTiles().getConcealedHand().size() == 0);
		next.giveTilesInConcealedHand(TestConstructHelper
				.set("N1,S1,W1,E1,r1,g1,w1,9c0,9c1,9c2,9s0,9s1,9s2"),
				KindOfWall.WALL);
		assertEquals(13, next.getTiles().getConcealedHand().size());

	}

	private boolean callValid(final IRound round, final IPlayer player) {
		final TreatmentChooseActionKong treatment = treatment();
		return treatment.valid();
	}

	private TreatmentChooseActionKong treatment() {
		final TreatmentChooseActionKong treatment = new TreatmentChooseActionKong(
				player, round);
		return treatment;
	}

	@Test
	public void valid() {
		final boolean valid = callValid(round, player);
		assertTrue(valid);

	}

	@Test
	public void invalidBecauseState() {

		player.setState(State.END);

		final boolean valid = callValid(round, player);
		assertFalse(valid);

	}

	@Test
	public void invalidBecausePlayerCantMakeAPung() {

		TestConstructHelper.clearConcealedHand(player);
		player.giveTilesInConcealedHand(TestConstructHelper
				.set("N1,S1,W1,E1,r1,g1,w1,9c0,9c1,9c2,9c3,6s0,8s0"),
				KindOfWall.WALL);
		assertEquals(13, player.getTiles().getConcealedHand().size());

		final boolean valid = callValid(round, player);
		assertFalse(valid);

	}

	@Test
	public void invalidBecause9StoneIsNotThrowed() {

		// mais le current n'a pas jeté un 9 stone (le connard)
		round.getTileSet().getDiscardedTiles().add(
				TestConstructHelper.tile("2s0"));

		final boolean valid = callValid(round, player);
		assertFalse(valid);

	}

	@Test
	public void invalidBecauseDiscardedTilesEmpty() {
		round.getTileSet().getDiscardedTiles().add(null);
		assertNull(round.getTileSet().lastDiscarded());

		final boolean valid = callValid(round, player);
		assertFalse(valid);
	}

	@Test
	public void changeState() {
		final TreatmentChooseActionKong trt = treatment();
		trt.changeStates();
		assertEquals(State.WAIT_OTHERS_ACTION, player.getState());
	}

	@Test
	public void getEatAction() {
		final TreatmentChooseActionKong trt = treatment();
		assertEquals(EatAction.KONG, trt.getEatAction());
	}
	//
	// @Test
	// public void doTreatment() {
	// treatment().doTreatment();
	// // the Kong is in opened hand
	// final Collection<ITile> theKong = player.getOpenedHand().iterator()
	// .next();
	// assertTrue(TileComputer.isPung(theKong));
	// assertTrue(CollectionUtils.isEqualCollection(TestConstructHelper
	// .set("9s0,9s1,9s2,9s3"), theKong));
	// // discarded tile is not anymore in discarded set
	// assertFalse(round.getTileSet().getDiscardedTiles().contains(
	// TestConstructHelper.tile("9s3")));
	// // others tiles are not in hand
	// assertFalse(CollectionUtils.containsAny(TestConstructHelper
	// .set("9s0,9s1,9s2,9s3"), player.getHand()));
	//
	// }
}
