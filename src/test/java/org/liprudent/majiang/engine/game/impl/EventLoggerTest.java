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
package org.liprudent.majiang.engine.game.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.apache.commons.lang.NotImplementedException;
import org.junit.Test;
import org.liprudent.majiang.engine.event.IEvent;
import org.liprudent.majiang.engine.event.IEventLogger;
import org.liprudent.majiang.engine.event.impl.EventLogger;
import org.liprudent.majiang.engine.player.IPlayer.Wind;

public class EventLoggerTest {

	private final class DummyEvent implements IEvent {
		int id;

		public DummyEvent(final int id) {
			super();
			this.id = id;
		}

		@Override
		public String getName() {
			throw new NotImplementedException();
		}

		@Override
		public Wind getPlayerWind() {
			throw new NotImplementedException();
		}

		@Override
		public IEvent publicView() {
			throw new NotImplementedException();
		}

		@Override
		public KindOfEvent getKind() {
			// TODO Auto-generated method stub
			return null;
		}

	}

	@Test
	public void read() {
		final IEventLogger logger1 = createEventLoggerWithFourDummyEvents();
		assertEquals(Integer.valueOf(4), logger1.nbUnread());
		readAllEvents(logger1);
		assertEquals(Integer.valueOf(0), logger1.nbUnread());

	}

	private static void readAllEvents(final IEventLogger logger1) {
		logger1.unreads();
	}

	@Test
	public void readEmpty() {
		final IEventLogger logger1 = new EventLogger();
		assertEquals(Integer.valueOf(0), logger1.nbUnread());
	}

	@Test
	public void isReadable() {
		final IEventLogger eventLogger = createEventLoggerWithFourDummyEvents();
		assertEquals(Integer.valueOf(4), eventLogger.nbUnread());
		List<IEvent> unreads = eventLogger.unreads();
		assertEquals(Integer.valueOf(0), eventLogger.nbUnread());
		for (final IEvent event : unreads) {
			assertNotNull(event);
		}
		for (int i = 1; i < 5; i++) {
			eventLogger.add(new DummyEvent(i));
		}
		assertEquals(Integer.valueOf(4), eventLogger.nbUnread());
		eventLogger.unreads();
		assertEquals(Integer.valueOf(0), eventLogger.nbUnread());
	}

	private IEventLogger createEventLoggerWithFourDummyEvents() {
		final IEventLogger logger1 = new EventLogger();
		for (int i = 1; i < 5; i++) {
			logger1.add(new DummyEvent(i));
		}
		return logger1;
	}
}
