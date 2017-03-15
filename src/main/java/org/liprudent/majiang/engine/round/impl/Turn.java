package org.liprudent.majiang.engine.round.impl;

import static org.liprudent.majiang.engine.round.State.HAVE_A_NEW_TILE;
import static org.liprudent.majiang.engine.round.State.WAIT_TILE_THROWED;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.liprudent.majiang.engine.event.KindOfWall;
import org.liprudent.majiang.engine.player.IPlayer;
import org.liprudent.majiang.engine.round.Scoring;
import org.liprudent.majiang.engine.round.State;
import org.liprudent.majiang.engine.round.impl.treatment.IEatAction;
import org.liprudent.majiang.engine.round.impl.treatment.IMahjongAction;
import org.liprudent.majiang.engine.round.impl.treatment.ITreatment;
import org.liprudent.majiang.engine.round.impl.treatment.ITreatmentDeclare;
import org.liprudent.majiang.engine.round.impl.treatment.ITreatmentHaveANewTile;
import org.liprudent.majiang.engine.round.impl.treatment.MaxEatActionComparator;
import org.liprudent.majiang.engine.round.impl.treatment.TreatmentChooseActionKong;
import org.liprudent.majiang.engine.tile.ITile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

public class Turn implements ITurn {

    private final Collection<IEatAction> eatActions;
    private int nbTreatments;
    /**
     * The "turn winner" is the player who eats the discarded tile. Null
     * otherwise (everybody pass).
     */
    private IPlayer turnWinner;
    private boolean endRoundBecauseMahjong = false;
    private boolean someoneHasMahjong = false;
    private boolean isKongTurnWinner = false;
    private boolean declaredKong = false;
    private IMahjongAction mahjongAction;
    private Logger log = LoggerFactory.getLogger(Turn.class);

    public Turn(IPlayer currentPlayer, ITile newTile, KindOfWall source) {
        super();
        this.currentPlayer = currentPlayer;
        this.actionPlayers = ImmutableList.of(currentPlayer.getNext(),
                currentPlayer.getNext().getNext(), currentPlayer.getNext()
                        .getNext().getNext());
        eatActions = new ArrayList<IEatAction>(4);
        nbTreatments = 0;
        if (newTile != null) {
            currentPlayer.giveTileInConcealedHand(newTile, source);
        }
        // 2. change state
        currentPlayer.setState(HAVE_A_NEW_TILE);
        for (IPlayer p : actionPlayers)
            p.setState(WAIT_TILE_THROWED);
    }

    public int getNbTreatments() {
        return nbTreatments;
    }

    /**
     * They are players who can pung, kong, chow or mahjong after currentPlayer
     * had discarded a tile
     */
    final List<IPlayer> actionPlayers;
    final IPlayer currentPlayer;

    @Override
    public boolean processTreatment(final ITreatment treatment) {
        boolean valid = doIfValid(treatment);
        if (endTurn()) {
            doEnd();
        }
        return valid;
    }

    /**
     * Proceed to eat action and set the turn winner
     */
    private void doEnd() {
        final IEatAction elected = electEatAction();
        if (elected != null) {
            if (elected instanceof IMahjongAction) {
                mahjongAction = (IMahjongAction) elected;
            }
            elected.eat();
            if (elected instanceof TreatmentChooseActionKong) {
                isKongTurnWinner = true;
            }
            turnWinner = elected.getPlayer();
        }
    }

    private IEatAction electEatAction() {
        if (eatActions != null && !eatActions.isEmpty()) {
            return Collections.max(eatActions, new MaxEatActionComparator());
        }
        return null;
    }

    public boolean endTurn() {
        log.debug("endTurn - [nbTreatments=" + nbTreatments
                + ",endRoundBecauseMahjong=" + endRoundBecauseMahjong
                + ",declaredKong=" + declaredKong + "]");
        return nbTreatments == 4 || endRoundBecauseMahjong || declaredKong;
    }

    private void addEatAction(final ITreatment treatment) {
        if (treatment instanceof IEatAction) {
            eatActions.add((IEatAction) treatment);
        }
    }

    /**
     * @param treatment
     * @return true if tratment is valid
     */
    private boolean doIfValid(final ITreatment treatment) {
        boolean valid = treatment.valid();
        if (valid) {
            treatment.doTreatment();
            if (treatment instanceof ITreatmentDeclare) {
                treatment.changeStates();
                declaredKong = true;
                turnWinner = treatment.getPlayer();
                if (treatment instanceof IMahjongAction) {
                    mahjongAction = (IMahjongAction) treatment;
                }
            } else {
                nbTreatments++;
                treatment.changeStates();
                computeSomeOneHasMahjong(treatment);
                computeEndRoundBecauseMahjong(treatment);
                addEatAction(treatment);
            }
            if (treatment instanceof IMahjongAction) {
                mahjongAction = (IMahjongAction) treatment;
            }
        } else {
            log.info("treatment is invalid : " + treatment.toString());
            // current player continue to play even if his treatment is invalid
            if (!(treatment instanceof ITreatmentHaveANewTile)) {
                treatment.getPlayer().setState(State.END);
                nbTreatments++;
            }
        }
        return valid;
    }

    private void computeSomeOneHasMahjong(final ITreatment treatment) {
        if (treatment instanceof IMahjongAction) {
            someoneHasMahjong = true;
            turnWinner = treatment.getPlayer();
            log.debug("computeSomeOneHasMahjong - [someoneHasMahjong={},winner={}]", someoneHasMahjong, turnWinner);
        }

    }

    /**
     * Set endRound to true in two cases:<br>
     * case 1: The player declare himself a mahjong (self drawn winning tile)<br>
     * case 2: Every opponents (otherPlayers) has choosed an action and one of
     * them is a mahjong<br>
     *
     * @param treatment
     */
    private void computeEndRoundBecauseMahjong(final ITreatment treatment) {
        log.debug("computeEndRoundBecauseMahjong - someoneHasMahjong="
                + someoneHasMahjong);
        if (treatment instanceof IMahjongAction) {
            final IMahjongAction endRoundAction = (IMahjongAction) treatment;
            // case 1:
            if (endRoundAction.immediateEnd()) {
                endRoundBecauseMahjong = true;
            }
        }

        // case 2
        if (someoneHasMahjong && nbTreatments == 4) {
            log.debug("computeEndRoundBecauseMahjong - case Every opponents (otherPlayers) has choosed an action and one of them is a mahjong");
            endRoundBecauseMahjong = true;
        }

    }

    @Override
    /**
     * The "turn winner" is the player who eats the discarded tile. Null
     * otherwise (everybody pass).
     */
    public IPlayer getTurnWinner() {
        return turnWinner;
    }

    @Override
    public boolean endRoundBecauseMahjong() {
        return endRoundBecauseMahjong;
    }

    @Override
    public boolean isKongTurnWinner() {
        return isKongTurnWinner || declaredKong;
    }

    public Collection<IEatAction> getEatActions() {
        return Collections.unmodifiableCollection(eatActions);
    }

    @Override
    public Scoring getScoring() {
        Preconditions.checkNotNull(turnWinner);
        return mahjongAction.getScoring();
    }

}
