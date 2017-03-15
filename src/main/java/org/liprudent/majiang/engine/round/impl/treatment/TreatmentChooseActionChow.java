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
import java.util.HashSet;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.collections.CollectionUtils;
import org.liprudent.majiang.engine.player.EatAction;
import org.liprudent.majiang.engine.player.IPlayer;
import org.liprudent.majiang.engine.round.IRound;
import org.liprudent.majiang.engine.round.State;
import org.liprudent.majiang.engine.tile.ITile;
import org.liprudent.majiang.engine.tile.ITileSets;
import org.liprudent.majiang.engine.tile.impl.TileComputer;

import com.google.common.collect.Maps;

public class TreatmentChooseActionChow extends AbstractTreatmentChooseActionEat
		implements IEatAction {

	private final IPlayer current;
	private final Collection<ITile> suit;
	private final SortedSet<ITile> twoTilesInHand;
	private final ITileSets tileSets;

	/**
	 * @param player
	 *            Player chowing
	 * @param partialSuit
	 *            Tiles of the suit owned by player
	 * @param round
	 */
	protected TreatmentChooseActionChow(final IPlayer player, final IRound round, final Collection<ITile> partialSuit) {
		super(player, round);
		current = round.getCurrentPlayer();
		suit = fullSuit(partialSuit, round);
		twoTilesInHand = new TreeSet<ITile>(CollectionUtils.intersection(player.getTiles().getConcealedHand(), suit));
		tileSets = round.getTileSet();
	}

	/**
	 * @param partialSuit
	 * @param round
	 * @return partialSuit + last discarded tile
	 */
	private Collection<ITile> fullSuit(final Collection<ITile> partialSuit, final IRound round) {
		final Collection<ITile> ret = new HashSet<ITile>();
		ret.addAll(partialSuit);
		ret.add(round.getTileSet().lastDiscarded());
		return ret;
	}

	@Override
	public void changeStates() {
		player.setState(State.WAIT_OTHERS_ACTION);

	}

	@Override
	public void doTreatment() {
	}

	@Override
	public boolean specificValid() {
		final boolean isSuit = TileComputer.isSequence(suit);
		final boolean haveTwo = checkPlayerHaveTwoTilesOfTheSuit();
		final boolean isGoodState = player.getState() == State.CHOOSE_ACTION;
		final boolean isNext = player.getPrevious() == current;
		final boolean oneTileIsThrowedAndNotOwned = oneTileIsThrowedAndNotOwned();
		final boolean specificValid = haveTwo && isGoodState && isNext && isSuit
		&& oneTileIsThrowedAndNotOwned;
		if(!specificValid){
			Map<String,Boolean> toPrint = Maps.newHashMap();
			toPrint.put("is it a Sequence? - suit : " + suit,isSuit);
			toPrint.put("player has 2 tiles? - hand : " + getPlayer().getTiles().getConcealedHand(), haveTwo);
			toPrint.put("player state is CHOOSE_ACTION?", isGoodState);
			toPrint.put("player is next of current?", isNext);
			toPrint.put("one tile of the chow is discarded?", oneTileIsThrowedAndNotOwned);
			log.info("Specific valid"+toPrint);
		}
		return specificValid;
	}

	private boolean oneTileIsThrowedAndNotOwned() {
		final Collection<ITile> only1Discarded = CollectionUtils.disjunction(
				twoTilesInHand, suit);
		if (only1Discarded.size() != 1) {
			return false;
		}
		final ITile theDiscardedTile = only1Discarded.iterator().next();
		return tileSets.lastDiscarded().equals(theDiscardedTile);
	}

	boolean checkPlayerHaveTwoTilesOfTheSuit() {

		if (twoTilesInHand.size() == 2) {
			return true;
		}
		return false;
	}

	@Override
	public EatAction getEatAction() {
		return EatAction.CHOW;
	}

	@Override
	protected SortedSet<ITile> ownedTiles() {
		return twoTilesInHand;
	}



}
