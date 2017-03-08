package org.liprudent.majiang.engine.round.impl.treatment;

import java.util.Collection;
import java.util.SortedSet;

import org.liprudent.majiang.engine.player.EatAction;
import org.liprudent.majiang.engine.player.IPlayer;
import org.liprudent.majiang.engine.round.IRound;
import org.liprudent.majiang.engine.tile.ITile;
import org.liprudent.majiang.engine.tile.impl.TileSorter;

public class TreatmentChooseActionPung extends
		AbstractTreatmentChooseEatTheSameKind implements IEatAction {

	protected TreatmentChooseActionPung(final IPlayer player, final IRound round) {
		super(player, round);
	}

	@Override
	protected Collection<SortedSet<ITile>> findPartialFigure(
			final SortedSet<ITile> hand) {
		return TileSorter.findPair(hand);
	}

	@Override
	public EatAction getEatAction() {
		return EatAction.PONG;
	}

}
