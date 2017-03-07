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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.liprudent.majiang.engine.player.IPlayer;
import org.liprudent.majiang.engine.round.IRound;
import org.liprudent.majiang.engine.round.State;
import org.liprudent.majiang.engine.tile.ITile;
import org.liprudent.majiang.engine.tile.impl.TileComputer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

/**
 * This is treatment to do when current player is in state HAVE_A_NEW_TILE and
 * his action is EXPOSED_KONG
 * 
 * @author jerome
 * 
 */
public class TreatmentHaveANewTileDeclareOpenKong extends
		AbstractTreatmentHaveANewTile implements ITreatment, ITreatmentDeclare {

	private final Logger log = LoggerFactory.getLogger(TreatmentHaveANewTileDeclareOpenKong.class);
	final Collection<ITile> declaredKong;

	final Collection<ITile> theOpenedPung;
	final IPlayer currentPlayer;

	protected TreatmentHaveANewTileDeclareOpenKong(final IPlayer player,
			final IRound round, final Collection<ITile> declaredKong) {
		super(player, round);
		Preconditions.checkArgument(declaredKong != null,"The declared kong can't be null");
		Preconditions.checkArgument(!declaredKong.isEmpty(),"The declared kong can't be empty");
		Preconditions.checkArgument(declaredKong.size() == 4,"The declared kong should have 4 tiles");
		this.declaredKong = declaredKong;

		currentPlayer = round.getCurrentPlayer();
		theOpenedPung = findOpenedPung();
	}

	Collection<ITile> findOpenedPung() {
		for (final Collection<ITile> openTiles : currentPlayer.getTiles()
				.getOpenedHand()) {
			if (TileComputer.haveIn(openTiles, declaredKong, 3)) {
				return openTiles;
			}
		}
		return null;
	}

	@Override
	public void changeStates() {
		player.setState(State.END);
		Iterator<IPlayer> it = player.othersIterator();
		while (it.hasNext()) {
			it.next().setState(State.END);
		}
	}

	@Override
	public void doTreatment() {
		final ITile theKongTile = findKongTileInConcealedHand();
		round.getCurrentPlayer().moveFromConcealedToOpen(theKongTile,
				theOpenedPung);

		// // move the kong tile from hand to opened hand
		// round.getCurrentPlayer().getConcealedHand().remove(theKongTile);
		// theOpenedPung.add(theKongTile);
		//
		// // give a tile from dead wall
		// round.getCurrentPlayer().getConcealedHand().add(
		// deadWallTile);
	}

	private ITile findKongTileInConcealedHand() {
		for(ITile tile:declaredKong){
			if(!theOpenedPung.contains(tile)) return tile;
		}
		throw new IllegalStateException("The kong tile can't be found in concealed hand - declaredKong:" + declaredKong + " - open pung : " + theOpenedPung + " - concealed hand :" + getPlayer().getTiles().getConcealedHand());
	}

	@Override
	public boolean specificValid() {

		// check the tiles are owned by current player
		final boolean ownedByCurrentPlayer = theOpenedPung!=null && !theOpenedPung.isEmpty();

		// check it's really a kong
		final boolean isKong = TileComputer.isKong(declaredKong);
		final boolean specificValid = ownedByCurrentPlayer && isKong;

		if(!specificValid){
			Map<String, Boolean> toLog = new HashMap<String, Boolean>(2);
			toLog.put("ownedByCurrentPlayer- open hand = " + getPlayer().getTiles().getOpenedHand(), ownedByCurrentPlayer);
			toLog.put("isKong - declared kong="+declaredKong, isKong);
			log.info("specificValid = " + toLog.toString());
		}
		return specificValid;
	}
}
