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

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.liprudent.majiang.engine.player.EatAction;
import org.liprudent.majiang.engine.player.IPlayer;

public class MaxEatActionComparator implements Comparator<IEatAction> {
	private static final Map<EatAction, Integer> actionStrength;
	static {
		actionStrength = new HashMap<EatAction, Integer>();
		actionStrength.put(EatAction.MAHJONG, 3);
		actionStrength.put(EatAction.KONG, 2);
		actionStrength.put(EatAction.PONG, 2);
		actionStrength.put(EatAction.CHOW, 1);
	}

	@Override
	public int compare(final IEatAction o1, final IEatAction o2) {
		assert o1.getCurrent() == o2.getCurrent();

		final int strength1 = actionStrength.get(o1.getEatAction());
		final int strength2 = actionStrength.get(o2.getEatAction());
		final int strengthDiff = strength1 - strength2;
		if (strengthDiff != 0) {
			return strengthDiff;
		} else {
			final IPlayer current = o1.getCurrent();
			final IPlayer player1 = o1.getPlayer();
			final IPlayer player2 = o2.getPlayer();
			for (IPlayer selected = current.getNext(); current != selected; selected = selected
					.getNext()) {
				if (selected == player1) {
					return 1;
				}
				if (selected == player2) {
					return -1;
				}
			}
			throw new AssertionError("Nearest player can't be found.");
		}
	}
}
