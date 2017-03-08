package org.liprudent.majiang.engine.round.impl.treatment;

import org.liprudent.majiang.engine.player.IPlayer;
import org.liprudent.majiang.engine.round.IRound;
import org.liprudent.majiang.engine.round.Scoring;
import org.liprudent.majiang.engine.round.State;
import org.liprudent.majiang.engine.round.impl.ScoringMcr;

import scala.collection.JavaConversions;

public class TreatmentHaveANewTileDeclareMahjong extends
        AbstractTreatmentHaveANewTile implements ITreatment, IMahjongAction {

    private final Scoring scoring;

    protected TreatmentHaveANewTileDeclareMahjong(final IPlayer player, final IRound round) {
        super(player, round);
        scoring = new ScoringMcr(JavaConversions.asScalaIterable(round.getVisibleTiles()), player.getTiles());
        System.out.println(scoring);
    }

    @Override
    public void changeStates() {
        for (final IPlayer other : round.getOtherPlayers()) {
            other.setState(State.END);
        }
        round.getCurrentPlayer().setState(State.END);

    }

    @Override
    public void doTreatment() {
    }

    @Override
    public boolean specificValid() {
        return scoring.isMahjong();
    }

    @Override
    public boolean immediateEnd() {
        return true;
    }

    @Override
    public Scoring getScoring() {
        return scoring;
    }
}
