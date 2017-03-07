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
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.liprudent.majiang.engine.event.KindOfWall;
import org.liprudent.majiang.engine.game.impl.TestConstructHelper;
import org.liprudent.majiang.engine.player.EatAction;
import org.liprudent.majiang.engine.player.IPlayer;
import org.liprudent.majiang.engine.player.WinningTile;
import org.liprudent.majiang.engine.round.IRound;
import org.liprudent.majiang.engine.round.State;

public class TreatmentChooseActionMahjongTest {
	IRound round;
	IPlayer player;
	TreatmentChooseActionMahjong trt;

	@Before
	public void init() {
		round = TestConstructHelper.constructGame().getLastRound();
		round.getCurrentPlayer().setState(State.END);
		// a guy had almost a mahjong missing 9b..
		player = round.getCurrentPlayer().getNext().getNext();
		player.setState(State.CHOOSE_ACTION);
		TestConstructHelper.clearConcealedHand(player);
		player.giveTilesInConcealedHand(TestConstructHelper
				.set("r0,r1,r3,g1,g2,g3,W1,W2,W3,1s0,2s0,3s0,9b0"),
				KindOfWall.WALL);
		assertEquals(13, player.getTiles().getConcealedHand().size());

		// and current player just throwed the missing tile ...
		round.getTileSet().getDiscardedTiles().add(TestConstructHelper.tile("9b1"));
		player.getTiles().setWinningTile(TestConstructHelper.tile("9b1"));
		
		trt = new TreatmentChooseActionMahjong(player, round);
	}

	@Test
	public void valid() {

		assertTrue(trt.valid());
	}

	@Test
	public void getEatAction() {
		assertEquals(EatAction.MAHJONG, trt.getEatAction());
	}
}
