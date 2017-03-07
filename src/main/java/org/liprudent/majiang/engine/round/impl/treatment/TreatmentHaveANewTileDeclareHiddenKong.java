/**
 * Majiang is a library that implements Mahjong game rules.
 * <p>
 * Copyright 2009 Prudent Jérome
 * <p>
 * This file is part of Majiang.
 * <p>
 * Majiang is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * Majiang is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * <p>
 * You can contact me at jprudent@gmail.com
 */
package org.liprudent.majiang.engine.round.impl.treatment;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.liprudent.majiang.engine.player.IPlayer;
import org.liprudent.majiang.engine.round.IRound;
import org.liprudent.majiang.engine.round.State;
import org.liprudent.majiang.engine.tile.ITile;
import org.liprudent.majiang.engine.tile.impl.TileComputer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is treatment to do when current player is in state HAVE_A_NEW_TILE and
 * his action is CONCEALED_KONG
 *
 * @author jerome
 */
public class TreatmentHaveANewTileDeclareHiddenKong extends AbstractTreatmentHaveANewTile
        implements ITreatment, ITreatmentDeclare {

    private final Logger log = LoggerFactory.getLogger(TreatmentHaveANewTileDeclareHiddenKong.class);
    final Collection<ITile> declaredKong;

    protected TreatmentHaveANewTileDeclareHiddenKong(final IPlayer player,
                                                     final IRound round,
                                                     final Collection<ITile> declaredKong) {
        super(player, round);
        this.declaredKong = declaredKong;
    }

    @Override
    public void changeStates() {
        player.setState(State.END);

        Iterator<IPlayer> it = player.othersIterator();
        while (it.hasNext()) {
            it.next().setState(State.END);
        }

    }

    /*
     * move kong to hidden hand and give a tile from dead wall
     */
    @Override
    public void doTreatment() {
        round.getCurrentPlayer().moveFromConcealedToHidden(declaredKong);
    }

    @Override
    public boolean specificValid() {
        // check the tiles are owned by current player
        final boolean ownedByCurrentPlayer = player.getTiles()
                .getConcealedHand().containsAll(declaredKong);

        // check it's really a kong
        final boolean isKong = TileComputer.isKong(declaredKong);
        final boolean specificValid = ownedByCurrentPlayer && isKong;
        if (!specificValid) {
            Map<String, Boolean> toLog = new HashMap<String, Boolean>(2);
            toLog.put("ownedByCurrentPlayer", ownedByCurrentPlayer);
            toLog.put("isKong", isKong);
            log.info("specificValid = " + toLog.toString());
        }
        return specificValid;
    }
}
