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

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.liprudent.majiang.engine.game.impl.TestConstructHelper;
import org.liprudent.majiang.engine.round.IRound;

public class MaxEatActionComparatorTest {
	MaxEatActionComparator comparator = new MaxEatActionComparator();

	@Test
	public void mahjongOverKong() {
		final IRound round = TestConstructHelper.constructGame().getLastRound();

		final IEatAction eatMahJong = new TreatmentChooseActionMahjong(round
				.getCurrentPlayer().getNext(), round);
		final IEatAction eatKong = new TreatmentChooseActionKong(round
				.getCurrentPlayer().getPrevious(), round);

		assertTrue(comparator.compare(eatMahJong, eatKong) >= 1);
		assertTrue(comparator.compare(eatKong, eatMahJong) <= 1);
	}

	@Test
	public void pungOverChow() {
		final IRound round = TestConstructHelper.constructGame().getLastRound();

		final IEatAction eatPung = new TreatmentChooseActionPung(round
				.getCurrentPlayer().getNext(), round);
		final IEatAction eatChow = new TreatmentChooseActionChow(round
				.getCurrentPlayer().getPrevious(), round, TestConstructHelper
				.set("1b0,2b0"));

		assertTrue(comparator.compare(eatPung, eatChow) >= 1);
		assertTrue(comparator.compare(eatChow, eatPung) <= 1);
	}

	@Test
	public void pungOverKong() {
		final IRound round = TestConstructHelper.constructGame().getLastRound();

		IEatAction eatPung = new TreatmentChooseActionPung(round
				.getCurrentPlayer().getNext(), round);
		IEatAction eatKong = new TreatmentChooseActionKong(round
				.getCurrentPlayer().getPrevious(), round);
		// the nearest player from current is elected
		assertTrue(comparator.compare(eatPung, eatKong) >= 1);
		assertTrue(comparator.compare(eatKong, eatPung) <= 1);

		eatPung = new TreatmentChooseActionPung(round.getCurrentPlayer()
				.getPrevious().getPrevious(), round);
		eatKong = new TreatmentChooseActionKong(round.getCurrentPlayer()
				.getNext(), round);
		// the nearest player from current is elected
		assertTrue(comparator.compare(eatPung, eatKong) <= 1);
		assertTrue(comparator.compare(eatKong, eatPung) >= 1);

	}
}
