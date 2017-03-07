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

import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.liprudent.majiang.engine.event.KindOfWall;
import org.liprudent.majiang.engine.game.IGame;
import org.liprudent.majiang.engine.game.impl.TestConstructHelper;
import org.liprudent.majiang.engine.player.IPlayer;
import org.liprudent.majiang.engine.round.State;
import org.liprudent.majiang.engine.tile.ITile;

public class TreatmentHaveANewTileDeclareOpenKongTest {
	private static final Collection<ITile> theKong = TestConstructHelper
			.set("1b0,1b1,1b2,1b3");
	private static final String pungString = "1b1,1b2,1b3";
	private SortedSet<ITile> pung;
	private IGame game;
	private IPlayer currentPlayer;
	private TreatmentHaveANewTileDeclareOpenKong trt;

	@Before
	public void init() {
		game = TestConstructHelper.constructGame();
		currentPlayer = game.getLastRound().getCurrentPlayer();
		pung = TestConstructHelper
				.set(TreatmentHaveANewTileDeclareOpenKongTest.pungString);
	}

	@Test(expected = IllegalArgumentException.class)
	public void constructor() {
		new TreatmentHaveANewTileDeclareOpenKong(null, null, null);
	}

	@Test
	public void valid() {

		prepareGoodHand();
		trt = new TreatmentHaveANewTileDeclareOpenKong(currentPlayer, game
				.getLastRound(),
				TreatmentHaveANewTileDeclareOpenKongTest.theKong);

		Assert.assertTrue(trt.valid());
	}

	@Test
	public void invalidBecauseBadHandNoPung() {

		prepareBadHandNoPung();
		trt = new TreatmentHaveANewTileDeclareOpenKong(currentPlayer, game
				.getLastRound(),
				TreatmentHaveANewTileDeclareOpenKongTest.theKong);
		Assert.assertFalse(trt.valid());
	}

	@Test
	public void invalidBecausePungNotOpened() {

		prepareBadHandClosedPung();
		trt = new TreatmentHaveANewTileDeclareOpenKong(currentPlayer, game
				.getLastRound(),
				TreatmentHaveANewTileDeclareOpenKongTest.theKong);
		Assert.assertFalse(trt.valid());
	}

	@Test
	public void invalidBecauseCurrentPlayerBadState() {

		prepareGoodHand();
		trt = new TreatmentHaveANewTileDeclareOpenKong(currentPlayer, game
				.getLastRound(),
				TreatmentHaveANewTileDeclareOpenKongTest.theKong);
		for (final State state : State.values()) {
			if (state != State.HAVE_A_NEW_TILE) {
				currentPlayer.setState(state);
				Assert.assertFalse(trt.valid());
			}
		}
	}

	@Test
	public void invalidBecauseOtherPlayersBadState() {

		prepareGoodHand();
		trt = new TreatmentHaveANewTileDeclareOpenKong(currentPlayer, game
				.getLastRound(),
				TreatmentHaveANewTileDeclareOpenKongTest.theKong);
		for (final State state : State.values()) {
			if (state != State.WAIT_TILE_THROWED) {
				currentPlayer.getNext().setState(state);
				Assert.assertFalse(trt.valid());
			}
		}
	}

	@Test
	public void invalidBecauseNotCurrent() {

		prepareGoodHand();
		trt = new TreatmentHaveANewTileDeclareOpenKong(currentPlayer.getNext(),
				game.getLastRound(),
				TreatmentHaveANewTileDeclareOpenKongTest.theKong);
		Assert.assertFalse(trt.valid());
	}

	@Test
	public void doTreatment() {
		prepareGoodHand();
		trt = new TreatmentHaveANewTileDeclareOpenKong(currentPlayer, game
				.getLastRound(),
				TreatmentHaveANewTileDeclareOpenKongTest.theKong);
		trt.doTreatment();

		Assert.assertFalse(CollectionUtils.containsAny(currentPlayer
				.getTiles().getConcealedHand(),
				TreatmentHaveANewTileDeclareOpenKongTest.theKong));
		Assert.assertTrue(CollectionUtils.isEqualCollection(
				TreatmentHaveANewTileDeclareOpenKongTest.theKong, currentPlayer
				.getTiles().getOpenedHand().iterator().next()));
	}

	@Test
	public void findOpenedPung() {
		prepareGoodHand();
		trt = new TreatmentHaveANewTileDeclareOpenKong(currentPlayer, game
				.getLastRound(),
				TreatmentHaveANewTileDeclareOpenKongTest.theKong);
		final Collection<ITile> openedPung = trt.findOpenedPung();
		Assert.assertTrue(CollectionUtils.isEqualCollection(pung, openedPung));
	}

	private void prepareBadHandClosedPung() {
		// give 10 tiles from wall in hand
		TestConstructHelper.clearConcealedHand(currentPlayer);
		final SortedSet<ITile> hand = TestConstructHelper
				.set("1c0,1c1,1c2,1c3,2c0,2c1,2c2,2c3,3c0,3c1,1b0,1b1,1b2,1b3");
		currentPlayer.giveTilesInConcealedHand(hand, KindOfWall.WALL);
		Assert.assertEquals(14, currentPlayer.getTiles().getConcealedHand().size());
	}

	private void prepareBadHandNoPung() {
		// give 10 tiles from wall in hand
		TestConstructHelper.clearConcealedHand(currentPlayer);
		final SortedSet<ITile> hand = TestConstructHelper
				.set("1c0,1c1,1c2,1c3,2c0,2c1,2c2,2c3,3c0,3c1,1b0");
		final SortedSet<ITile> pung = TestConstructHelper.set("2b1,2b2,2b3");
		currentPlayer.giveTilesInConcealedHand(hand, KindOfWall.WALL);

		// give opened
		currentPlayer.giveTilesInConcealedHand(pung, KindOfWall.WALL);
		currentPlayer.moveFromConcealedToOpen(pung);

		Assert.assertEquals(11, currentPlayer.getTiles().getConcealedHand().size());
		Assert.assertEquals(3, currentPlayer.getTiles().getOpenedHand().iterator().next()
				.size());
	}

	private void prepareGoodHand() {
		// give 10 tiles from wall in hand
		TestConstructHelper.clearConcealedHand(currentPlayer);
		final SortedSet<ITile> hand = TestConstructHelper
				.set("1c0,1c1,1c2,1c3,2c0,2c1,2c2,2c3,3c0,3c1,1b0");
		currentPlayer.giveTilesInConcealedHand(hand, KindOfWall.WALL);

		// give opened
		currentPlayer.giveTilesInConcealedHand(pung, KindOfWall.WALL);
		currentPlayer.moveFromConcealedToOpen(pung);
		Assert.assertEquals(11, currentPlayer.getTiles().getConcealedHand().size());
		Assert.assertEquals(3, currentPlayer.getTiles().getOpenedHand().iterator().next()
				.size());
	}

}
