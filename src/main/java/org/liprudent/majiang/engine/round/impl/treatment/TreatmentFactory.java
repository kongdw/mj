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

import org.liprudent.majiang.engine.player.IPlayer;
import org.liprudent.majiang.engine.round.Action;
import org.liprudent.majiang.engine.round.IRound;
import org.liprudent.majiang.engine.tile.ITile;

abstract public class TreatmentFactory {
	private TreatmentFactory() {
	}

	public static ITreatment getInstance(final IPlayer player,
			final Action action, final IRound round,
			final Collection<ITile> tiles) {
		if (action == Action.EXPOSED_KONG) {
			return new TreatmentHaveANewTileDeclareOpenKong(player, round,
					tiles);
		}
		if (action == Action.CONCEALED_KONG) {
			return new TreatmentHaveANewTileDeclareHiddenKong(player, round,
					tiles);
		}

		if (action == Action.CHOW) {
			return new TreatmentChooseActionChow(player, round, tiles);
		}

		throw new AssertionError("Action is unknown");
	}

	public static ITreatment getInstance(final IPlayer player,
			final Action action, final IRound round, final ITile tile) {

		if (action == Action.DISCARD_TILE) {
			return new TreatmentHaveANewTileDiscardTile(player, round, tile);
		}
		throw new AssertionError("Action is unknown");
	}

	public static ITreatment getInstance(final IPlayer player,final Action action, final IRound round) {
		if (action == Action.DECLARE_MAHJONG) {
			return new TreatmentHaveANewTileDeclareMahjong(player, round);
		}
		if (action == Action.ACTION_MAHJONG) {
			return new TreatmentChooseActionMahjong(player, round);
		}

		if (action == Action.PASS) {
			return new TreatmentChooseActionPass(player, round);
		}

		if (action == Action.PONG) {
			return new TreatmentChooseActionPung(player, round);
		}

		if (action == Action.KONG) {
			return new TreatmentChooseActionKong(player, round);
		}

		throw new AssertionError("Action is unknown");
	}
}
