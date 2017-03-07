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

import org.liprudent.majiang.engine.player.IPlayer;
import org.liprudent.majiang.engine.round.IRound;
import static org.liprudent.majiang.engine.round.State.*;
import org.liprudent.majiang.engine.tile.ITile;


/**
 * This is treatment to do when current player is in state HAVE_A_NEW_TILE and
 * his action is DISCARD_TILE
 * 
 * 
 */
public class TreatmentHaveANewTileDiscardTile extends
		AbstractTreatmentHaveANewTile implements ITreatment {

	final ITile throwedTile;

	protected TreatmentHaveANewTileDiscardTile(final IPlayer player,
			final IRound round, final ITile throwedTile) {
		super(player, round);
		this.throwedTile = throwedTile;
	}

	@Override
	public void changeStates() {
		round.getCurrentPlayer().setState(END);
		for(IPlayer player : round.getOtherPlayers()){
			player.setState(CHOOSE_ACTION);
		}
	}

	/*
	 * move the tile from current player hand to discardedTile
	 */
	@Override
	public void doTreatment() {
		//remove tile from player's tiles
		round.getCurrentPlayer().discardTile(throwedTile);
		//add it to the list of throwed tiles
		round.getTileSet().getDiscardedTiles().add(throwedTile);
		//set potential winning tiles to other players
		Iterator<IPlayer> it = player.othersIterator();
		while(it.hasNext()){
			it.next().getTiles().setWinningTile(throwedTile);
		}

	}

	@Override
	public boolean specificValid() {
		// check the throwed tile is owned by current player
		return player.getTiles().getConcealedHand().contains(throwedTile);
	}
}
