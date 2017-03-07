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


import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.liprudent.majiang.engine.game.IGame;
import org.liprudent.majiang.engine.player.IPlayer;
import org.liprudent.majiang.engine.round.IRound;
import org.liprudent.majiang.engine.round.State;
import org.liprudent.majiang.engine.tile.ITile;
import org.liprudent.majiang.engine.tile.impl.TileSuitBamboo;
import org.liprudent.majiang.engine.game.impl.TestConstructHelper;

public class TreatmentHaveANewTileDeclareHiddenKongTest {
	final IGame game = TestConstructHelper.constructSpecialKongGame();
	final IRound round = game.getLastRound();
	final IPlayer current = round.getCurrentPlayer();
	Set<ITile> kong = TestConstructHelper.kong(1);

	@Test
	public void changeStates() {
		assertEquals(State.HAVE_A_NEW_TILE, round.getCurrentPlayer().getState());
		assertEquals(14, round.getCurrentPlayer().getTiles().getConcealedHand().size());
		for (final IPlayer player : round.getOtherPlayers()) {
			assertEquals(State.WAIT_TILE_THROWED, player.getState());
			assertEquals(13, player.getTiles().getConcealedHand().size());
		}

		final ITreatment treatment = new TreatmentHaveANewTileDeclareHiddenKong(
				current, round, TestConstructHelper.kong(1));
		treatment.changeStates();

		assertEquals(State.END, round.getCurrentPlayer().getState());
		for (final IPlayer player : round.getOtherPlayers()) {
			assertEquals(State.END, player.getState());
		}
	}

	@Test
	public void doTreatment() {

		
		assertTrue(round.getCurrentPlayer().getTiles().getConcealedHand().containsAll(kong));

		final ITreatment treatment = new TreatmentHaveANewTileDeclareHiddenKong(
				current, round, TestConstructHelper.kong(1));
		treatment.doTreatment();

		// kong is not in the hand
		assertFalse(round.getCurrentPlayer().getTiles().getConcealedHand().containsAll(kong));
		// kong is hidden
		assertTrue(round.getCurrentPlayer().getTiles().getHiddenHand().contains(kong));
	}

	@Test
	public void valid() {

		// Valid
		final ITreatment treatment = new TreatmentHaveANewTileDeclareHiddenKong(
				current, round, kong);
		assertTrue(treatment.valid());

	}

	@Test
	public void invalidCurrentPlayerState() {

		round.getCurrentPlayer().setState(State.WAIT_OTHERS_ACTION);

		final ITreatment treatment = new TreatmentHaveANewTileDeclareHiddenKong(
				current, round, kong);
		assertFalse(treatment.valid());

	}

	@Test
	public void invalidCurrentOtherPlayerState() {

		round.getCurrentPlayer().getNext().setState(State.WAIT_OTHERS_ACTION);

		final ITreatment treatment = new TreatmentHaveANewTileDeclareHiddenKong(
				current, round, kong);
		assertFalse(treatment.valid());

	}

	@Test
	public void invalidNoKongNotOwned() {
		// bad: this tile is not owned by current player
		final IGame game = TestConstructHelper.constructGame();
		final IRound round = game.getLastRound();
		final ITreatment treatment = new TreatmentHaveANewTileDeclareHiddenKong(
				current, round, kong);
		assertFalse(treatment.valid());
	}

	@Test
	public void invalidNoKong1() {
		final Set<ITile> notAKong = new HashSet<ITile>();
		notAKong.add(new TileSuitBamboo(0, 1));
		notAKong.add(new TileSuitBamboo(1, 1));

		final ITreatment treatment = new TreatmentHaveANewTileDeclareHiddenKong(
				current, round, notAKong);
		assertFalse(treatment.valid());

	}

	@Test
	public void invalidNotCurrent() {

		final ITreatment treatment = new TreatmentHaveANewTileDeclareHiddenKong(
				current.getNext(), round, kong);
		assertFalse(treatment.valid());

	}

	@Test
	public void invalidNoKong2() {
		final Set<ITile> notAKong = new HashSet<ITile>();
		notAKong.add(new TileSuitBamboo(0, 1));
		notAKong.add(new TileSuitBamboo(0, 2));
		notAKong.add(new TileSuitBamboo(0, 3));
		notAKong.add(new TileSuitBamboo(0, 4));

		final ITreatment treatment = new TreatmentHaveANewTileDeclareHiddenKong(
				current, round, notAKong);
		assertFalse(treatment.valid());

	}
}
