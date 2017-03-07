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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.NotImplementedException;
import org.liprudent.majiang.engine.event.IEvent;
import org.liprudent.majiang.engine.event.IEventLogger;

import com.google.common.base.Preconditions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventLogger implements IEventLogger {

	private final List<IEvent> events = new ArrayList<IEvent>(100);
	private int cptRead = 0;
	private final Logger log = LoggerFactory.getLogger(EventLogger.class);

	@Override
	public Integer nbUnread() {
		return events.size() - cptRead;
	}

	@Override
	public void add(final IEvent event) {
		Preconditions.checkNotNull(event, "Can't add null event");
		log.debug("Adding an event " + event.toString());
		events.add(event);
	}

	@Override
	public List<IEvent> unreads() {
		final List<IEvent> ret = events.subList(cptRead, events.size());
		cptRead += ret.size();
		return ret;
	}

}
