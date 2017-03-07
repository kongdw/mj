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
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.liprudent.majiang.engine.round.impl.treatment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.liprudent.majiang.engine.game.IGame;
import org.liprudent.majiang.engine.game.impl.TestConstructHelper;
import org.liprudent.majiang.engine.player.IPlayer;
import org.liprudent.majiang.engine.round.IRound;
import org.liprudent.majiang.engine.round.State;

/**
 * 
 * @author jerome
 */
public class TreatmentChooseActionPassTest {

	final IGame game = TestConstructHelper.constructGame();
	final IRound round = game.getLastRound();

	public TreatmentChooseActionPassTest() {
	}

	/**
	 * Test of valid method, of class TreatmentChooseActionPass.
	 */
	@Test
	public void testValid() {

		System.out.println("valid");
		final TreatmentChooseActionPass instance = normalCaseFixture();

		final boolean expResult = true;
		final boolean result = instance.valid();

		assertEquals(expResult, result);

	}

	/**
	 * Test of doTreatment method, of class TreatmentChooseActionPass.
	 */
	@Test
	public void testDoTreatment() {
		System.out.println("doTreatment");
		final TreatmentChooseActionPass instance = normalCaseFixture();
		instance.doTreatment();
	}

	/**
	 * Test of changeStates method, of class TreatmentChooseActionPass.
	 */
	@Test
	public void testChangeStates() {
		System.out.println("changeStates");
		final TreatmentChooseActionPass instance = normalCaseFixture();
		instance.changeStates();
		assertTrue(instance.player.getState() == State.END);
	}

	private TreatmentChooseActionPass normalCaseFixture() {
		// fixtures
		final IPlayer player = round.getCurrentPlayer().getNext();
		player.setState(State.CHOOSE_ACTION);
		round.getCurrentPlayer().setState(State.END);
		final TreatmentChooseActionPass instance = new TreatmentChooseActionPass(
				player, round);
		return instance;
	}

}