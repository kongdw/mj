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

import java.util.Iterator;

import org.junit.Assert;
import org.junit.Test;
import org.liprudent.majiang.engine.game.IGame;
import org.liprudent.majiang.engine.game.impl.TestConstructHelper;
import org.liprudent.majiang.engine.player.IPlayer;
import org.liprudent.majiang.engine.round.IRound;
import org.liprudent.majiang.engine.round.State;
import org.liprudent.majiang.engine.tile.ITile;

public class TreatmentHaveANewTileDiscardTileTest {
	final IGame game = TestConstructHelper.constructGame();
	final IRound round = game.getLastRound();
	ITile discardedTile = round.getCurrentPlayer().getTiles().getConcealedHand().iterator().next();
	final IPlayer currentPlayer = round.getCurrentPlayer();

	// @Test
	// public void changeStates() {
	// Assert.assertEquals(State.HAVE_A_NEW_TILE, this.round
	// .getCurrentPlayer().getState());
	//
	// final ITreatment treatment = new TreatmentHaveANewTileDiscardTile(
	// this.round, this.discardedTile);
	// treatment.changeStates();
	// Assert
	// .assertEquals(State.END, this.round.getCurrentPlayer()
	// .getState());
	// for (final IPlayer player : this.round.getOtherPlayers()) {
	// Assert.assertEquals(State.CHOOSE_ACTION, player.getState());
	// }
	// }

	@Test
	public void doTreatment() {
		Assert.assertTrue(round.getCurrentPlayer().getTiles().getConcealedHand().contains(
				discardedTile));
		Assert.assertFalse(round.getTileSet().getDiscardedTiles().contains(
				discardedTile));
		
		final ITreatment treatment = new TreatmentHaveANewTileDiscardTile(
				currentPlayer, round, discardedTile);
		treatment.doTreatment();
		
		Assert.assertFalse(round.getCurrentPlayer().getTiles().getConcealedHand().contains(
				discardedTile));
		Assert.assertTrue(round.getTileSet().getDiscardedTiles().contains(
				discardedTile));
		
		Iterator<IPlayer> it =currentPlayer.othersIterator(); 
		while(it.hasNext()){
			Assert.assertEquals("The discarded tile should be set in winnnigTile of otherPlayers",discardedTile,it.next().getTiles().getWinningTile().tile());
		}
		
	}

	@Test
	public void valid() {

		// Valid
		final ITreatment treatment = new TreatmentHaveANewTileDiscardTile(
				currentPlayer, round, discardedTile);
		Assert.assertTrue(treatment.valid());

	}

	@Test
	public void invalidCurrentPlayerState() {

		round.getCurrentPlayer().setState(State.WAIT_OTHERS_ACTION);

		final ITreatment treatment = new TreatmentHaveANewTileDiscardTile(
				currentPlayer, round, discardedTile);
		Assert.assertFalse(treatment.valid());

	}

	@Test
	public void invalidCurrentOtherPlayerState() {

		round.getCurrentPlayer().getNext().setState(State.WAIT_OTHERS_ACTION);

		final ITreatment treatment = new TreatmentHaveANewTileDiscardTile(
				currentPlayer, round, discardedTile);
		Assert.assertFalse(treatment.valid());

	}

	@Test
	public void invalidTileThrowed() {

		// bad: this tile is not owned by current player
		discardedTile = round.getTileSet().getDeadWall().get(0);

		final ITreatment treatment = new TreatmentHaveANewTileDiscardTile(
				currentPlayer, round, discardedTile);
		Assert.assertFalse(treatment.valid());

	}

	@Test
	public void invalidPlayer() {

		final ITreatment treatment = new TreatmentHaveANewTileDiscardTile(
				currentPlayer.getNext(), round, discardedTile);
		Assert.assertFalse(treatment.valid());

	}

}
