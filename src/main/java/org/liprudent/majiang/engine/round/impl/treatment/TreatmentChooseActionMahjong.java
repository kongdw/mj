package org.liprudent.majiang.engine.round.impl.treatment;

import java.util.SortedSet;

import org.liprudent.majiang.engine.player.EatAction;
import org.liprudent.majiang.engine.player.IPlayer;
import org.liprudent.majiang.engine.round.IRound;
import org.liprudent.majiang.engine.round.Scoring;
import org.liprudent.majiang.engine.round.State;
import org.liprudent.majiang.engine.round.impl.ScoringMcr;
import org.liprudent.majiang.engine.tile.ITile;

import scala.collection.JavaConversions;

public class TreatmentChooseActionMahjong extends
		AbstractTreatmentChooseActionEat implements IEatAction, IMahjongAction {
	final ITile lastDiscarded;
	private Scoring scoring; 

	protected TreatmentChooseActionMahjong(final IPlayer player,
			final IRound round) {
		super(player, round);
		lastDiscarded = round.getTileSet().lastDiscarded();
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
		final ScoringMcr scoring = new ScoringMcr(JavaConversions
				.asIterable(round.getVisibleTiles()), player.getTiles());
		final boolean isMahjong = scoring.isMahjong();
		if(isMahjong) this.scoring = scoring;
		return isMahjong;
	}

	@Override
	public EatAction getEatAction() {
		return EatAction.MAHJONG;
	}

	@Override
	protected SortedSet<ITile> ownedTiles() {
		return player.getTiles().getConcealedHand();
	}

	@Override
	public boolean immediateEnd() {
		return false;
	}

	@Override
	public Scoring getScoring() {
		return scoring;
	}
}
