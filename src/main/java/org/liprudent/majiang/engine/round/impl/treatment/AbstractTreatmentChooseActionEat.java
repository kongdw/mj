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

import java.util.SortedSet;

import org.liprudent.majiang.engine.player.IPlayer;
import org.liprudent.majiang.engine.round.IRound;
import org.liprudent.majiang.engine.tile.ITile;

public abstract class AbstractTreatmentChooseActionEat extends
		AbstractTreatmentChooseAction implements IEatAction {

	protected AbstractTreatmentChooseActionEat(final IPlayer player,
			final IRound round) {
		super(player, round);
	}

	@Override
	public IPlayer getCurrent() {
		return round.getCurrentPlayer();
	}

	@Override
	public void eat() {
		final ITile lastDiscarded = round.getTileSet().lastDiscarded();
		final SortedSet<ITile> ownedTiles = ownedTiles();
		log.debug("eat() - [player="+player+",ownedTiles="+ownedTiles+",discardedTile="+lastDiscarded+"]");

		// move player's owned tiles in openHand
		player.moveFromConcealedToOpen(ownedTiles);

		// move the discarded tile in openedHand:
		round.getTileSet().removeLastDiscardedTile();
		player.moveDiscardedTileInOpen(lastDiscarded, ownedTiles, getCurrent().getWind());

	}

	/**
	 * @return The tiles owned by the player (not including discarded tile)
	 */
	abstract protected SortedSet<ITile> ownedTiles();

}
