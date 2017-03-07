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

import org.junit.Test;
import org.liprudent.majiang.engine.event.KindOfWall;
import org.liprudent.majiang.engine.game.impl.TestConstructHelper;
import org.liprudent.majiang.engine.player.IPlayer;
import org.liprudent.majiang.engine.round.IRound;
import org.liprudent.majiang.engine.round.State;

public class TreatmentHaveANewTileDeclareMahjongTest {

	private static final String NOT_MAHJONG_HAND = "r0,w1,r3,g1,g2,g3,W1,W2,W3,1s0,2s0,3s0,9b0,9b1";
	private static final String MAHJONG_HAND = "r0,r1,r3,g1,g2,g3,W1,W2,W3,1s0,2s0,3s0,9b0,9b1";

	@Test
	public void valid() {
		final IRound round = TestConstructHelper.constructGame().getLastRound();
		final IPlayer current = round.getCurrentPlayer();
		TestConstructHelper.clearConcealedHand(current);
		current.giveTilesInConcealedHand(TestConstructHelper.set(MAHJONG_HAND),
				KindOfWall.WALL);
		assertEquals(14, current.getTiles().getConcealedHand().size());
		final TreatmentHaveANewTileDeclareMahjong trt = new TreatmentHaveANewTileDeclareMahjong(
				current, round);
		assertTrue(trt.valid());
	}

	@Test
	public void invalidBacauseNotGoodState() {
		final IRound round = TestConstructHelper.constructGame().getLastRound();
		final IPlayer current = round.getCurrentPlayer();
		TestConstructHelper.clearConcealedHand(current);
		current.giveTilesInConcealedHand(TestConstructHelper.set(MAHJONG_HAND),
				KindOfWall.WALL);
		assertEquals(14, current.getTiles().getConcealedHand().size());
		current.setState(State.WAIT_OTHERS_ACTION);
		final TreatmentHaveANewTileDeclareMahjong trt = new TreatmentHaveANewTileDeclareMahjong(
				current, round);
		assertFalse(trt.valid());
	}

	@Test
	public void invalidBecauseNotCurrent() {
		final IRound round = TestConstructHelper.constructGame().getLastRound();
		final IPlayer next = round.getCurrentPlayer().getNext();
		TestConstructHelper.clearConcealedHand(next);
		next.giveTilesInConcealedHand(TestConstructHelper.set(MAHJONG_HAND),
				KindOfWall.WALL);
		assertEquals(14, next.getTiles().getConcealedHand().size());
		final TreatmentHaveANewTileDeclareMahjong trt = new TreatmentHaveANewTileDeclareMahjong(
				next, round);
		assertFalse(trt.valid());
	}

	@Test
	public void invalidBecauseMahjong() {
		final IRound round = TestConstructHelper.constructGame().getLastRound();
		final IPlayer current = round.getCurrentPlayer();
		TestConstructHelper.clearConcealedHand(current);
		current.giveTilesInConcealedHand(TestConstructHelper
				.set(NOT_MAHJONG_HAND), KindOfWall.WALL);
		assertEquals(14, current.getTiles().getConcealedHand().size());
		final TreatmentHaveANewTileDeclareMahjong trt = new TreatmentHaveANewTileDeclareMahjong(
				current, round);
		assertFalse(trt.valid());
	}

	@Test
	public void changeStates() {
		final IRound round = TestConstructHelper.constructGame().getLastRound();
		final IPlayer current = round.getCurrentPlayer();
		TestConstructHelper.clearConcealedHand(current);
		current.giveTilesInConcealedHand(TestConstructHelper.set(MAHJONG_HAND),
				KindOfWall.WALL);
		assertEquals(14, current.getTiles().getConcealedHand().size());
		final TreatmentHaveANewTileDeclareMahjong trt = new TreatmentHaveANewTileDeclareMahjong(
				current, round);
		trt.changeStates();
		for (final IPlayer other : round.getOtherPlayers()) {
			assertEquals(State.END, other.getState());
		}
		assertEquals(State.END, round.getCurrentPlayer().getState());

	}
}
