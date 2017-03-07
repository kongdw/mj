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

import org.liprudent.majiang.engine.player.IPlayer;
import org.liprudent.majiang.engine.round.IRound;
import org.liprudent.majiang.engine.round.State;
import org.liprudent.majiang.engine.tile.ITile;

abstract public class AbstractTreatmentChooseEatTheSameKind extends
		AbstractTreatmentChooseActionEat {
	private final ITile lastDiscardedTile;
	private Collection<ITile> partialFigure;

	protected AbstractTreatmentChooseEatTheSameKind(final IPlayer player,
			final IRound round) {
		super(player, round);
		lastDiscardedTile = round.getTileSet().lastDiscarded();
	}

	private SortedSet<ITile> findPartialFigure() {
		final Collection<SortedSet<ITile>> partialFigures = findPartialFigure(player
				.getTiles().getConcealedHand());
		for (final SortedSet<ITile> partialFigure : partialFigures) {
			if (partialFigure.iterator().next().compare(lastDiscardedTile)) {
				return partialFigure;
			}
		}
		return null;
	}

	abstract protected Collection<SortedSet<ITile>> findPartialFigure(
			SortedSet<ITile> hand);

	@Override
	public void changeStates() {
		player.setState(State.WAIT_OTHERS_ACTION);

	}

	@Override
	public void doTreatment() {
		// throw new NotImplementedException();
	}

	@Override
	public boolean specificValid() {
		partialFigure = findPartialFigure();

		// we need to check we have a partial figure in hand
		final boolean isFigure = partialFigure != null;
		if (!isFigure) {
			log.info("Player doesn't own a partial figure - partial figure : " + partialFigure + "- hand : " + getPlayer().getTiles().getConcealedHand() + 
					" - last discarded : " + lastDiscardedTile);
		}
		// a tile has been throwed
		final boolean oneTileIsThrowedAndNotOwned = lastDiscardedTile != null;
		if (!oneTileIsThrowedAndNotOwned) {
			log.info("None tiles has been throwed");
		}

		return isFigure && oneTileIsThrowedAndNotOwned;
	}

	@Override
	protected SortedSet<ITile> ownedTiles() {
		return findPartialFigure();
	}
}
