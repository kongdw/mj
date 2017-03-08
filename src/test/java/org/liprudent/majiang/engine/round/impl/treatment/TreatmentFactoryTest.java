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

import org.junit.Assert;
import org.junit.Test;
import org.liprudent.majiang.engine.game.impl.TestConstructHelper;
import org.liprudent.majiang.engine.round.Action;
import org.liprudent.majiang.engine.round.IRound;
import org.liprudent.majiang.engine.tile.impl.TileHonorWhite;

public class TreatmentFactoryTest {
	final IRound round = TestConstructHelper.constructGame().getLastRound();

	@Test
	public void valueOfWithCollectionOfTiles() {
		Assert.assertEquals(TreatmentHaveANewTileDeclareOpenKong.class,
				treatment(round, Action.EXPOSED_KONG));
		Assert.assertEquals(TreatmentHaveANewTileDeclareHiddenKong.class,
				treatment(round, Action.EXPOSED_KONG));

		Assert.assertEquals(TreatmentChooseActionChow.class, treatment(round,
				Action.CHOW));

	}

	@Test
	public void valueOfWithOneTile() {
		Assert.assertEquals(TreatmentHaveANewTileDiscardTile.class,
				TreatmentFactory.getInstance(round.getCurrentPlayer(),
						Action.DISCARD_TILE, round, new TileHonorWhite(2))
						.getClass());
	}

	@Test
	public void valueOfWithNotTilesAtAll() {
		Assert.assertEquals(TreatmentChooseActionMahjong.class,
				TreatmentFactory.getInstance(round.getCurrentPlayer(),
						Action.ACTION_MAHJONG, round).getClass());
		Assert.assertEquals(TreatmentChooseActionPass.class, TreatmentFactory
				.getInstance(round.getCurrentPlayer(), Action.PASS, round)
				.getClass());
		Assert.assertEquals(TreatmentHaveANewTileDeclareMahjong.class,
				TreatmentFactory.getInstance(round.getCurrentPlayer(),
						Action.DECLARE_MAHJONG, round).getClass());
		Assert.assertEquals(TreatmentChooseActionPung.class, TreatmentFactory
				.getInstance(round.getCurrentPlayer(), Action.PONG, round)
				.getClass());
		Assert.assertEquals(TreatmentChooseActionKong.class, TreatmentFactory
				.getInstance(round.getCurrentPlayer(), Action.PONG, round)
				.getClass());
	}

	private Class<? extends ITreatment> treatment(final IRound round,
			final Action action) {
		return TreatmentFactory.getInstance(round.getCurrentPlayer(), action,
				round, TestConstructHelper.kong(2)).getClass();
	}
}
