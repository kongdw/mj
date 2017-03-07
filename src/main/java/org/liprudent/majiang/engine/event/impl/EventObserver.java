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
package org.liprudent.majiang.engine.event.impl;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import org.liprudent.majiang.engine.event.IEvent;
import org.liprudent.majiang.engine.event.IEventLogger;
import org.liprudent.majiang.engine.event.IEventObserver;
import org.liprudent.majiang.engine.event.NoBrodcastEvent;
import org.liprudent.majiang.engine.player.IPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

public class EventObserver implements Observer, IEventObserver {

	private Map<String, IEventLogger> eventLoggers;

	private final Logger log = LoggerFactory.getLogger(EventObserver.class);

	public EventObserver(final Set<IPlayer> players) {
		eventLoggers = new HashMap<String, IEventLogger>();
		valid(players);
		for (final IPlayer p : players) {
			eventLoggers.put(p.getName(), new EventLogger());
		}
		// post cond
		if (eventLoggers.size() != 4) {
			throw new IllegalStateException("The map should contain 4 entries");
		}
	}

	private void valid(final Set<IPlayer> players) {
		if (players.size() != 4) {
			throw new IllegalArgumentException("Must provide 4 players");
		}

		for (final IPlayer player : players) {
			if (player == null) {
				throw new IllegalArgumentException("Players can't be null");
			}
		}
	}

	@Override
	public void update(final Observable player, final Object arg1) {
		Preconditions.checkNotNull(player);
		Preconditions.checkNotNull(arg1);
		Preconditions.checkArgument(player instanceof IPlayer);
		Preconditions.checkArgument(arg1 instanceof IEvent);
		final String name = ((IPlayer) player).getName();
		Preconditions.checkArgument(eventLoggers.containsKey(name));

		for (final String playerKey : eventLoggers.keySet()) {
			final IEvent event = (IEvent) arg1;
			if (playerKey.equals(name)) {
				eventLoggers.get(name).add(event);
			} else {// Broadcast the public view of the event
				boolean noBraodcast = arg1 instanceof NoBrodcastEvent;
				if (!noBraodcast) {
					IEvent publicView = event.publicView();
					eventLoggers.get(playerKey).add(publicView);
				}
			}
		}

	}

	/**
	 * TODO unittest
	 */
	@Override
	public IEventLogger eventLogger(final String playerName) {
		Preconditions.checkState(eventLoggers.size() == 4,
				"event logger doesn't have a size of 4");
		log.debug("eventLogger() " + eventLoggers.toString());
		Preconditions.checkState(eventLoggers.containsKey(playerName));
		assert eventLoggers.containsKey(playerName);
		assert eventLoggers.get(playerName) != null;
		log.debug("Trying to get logger for " + playerName + "in "
				+ eventLoggers);
		return eventLoggers.get(playerName);
	}

//	private void writeObject(ObjectOutputStream out) throws IOException {
//		out.defaultWriteObject();
//		log.debug("size = " + eventLoggers.size());
//		log.debug("logger = " + eventLoggers);
//	}
}
