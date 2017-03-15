package org.liprudent.majiang.engine.event.impl;

import com.google.common.base.Preconditions;
import org.liprudent.majiang.engine.event.IEvent;
import org.liprudent.majiang.engine.event.IEventLogger;
import org.liprudent.majiang.engine.event.IEventObserver;
import org.liprudent.majiang.engine.event.NoBroadcastEvent;
import org.liprudent.majiang.engine.player.IPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


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
                boolean noBroadcast = arg1 instanceof NoBroadcastEvent;
                if (!noBroadcast) {
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
        Preconditions.checkState(eventLoggers.size() == 4, "event logger doesn't have a size of 4");
        log.debug("eventLogger() {}", eventLoggers.toString());
        Preconditions.checkState(eventLoggers.containsKey(playerName));
        assert eventLoggers.containsKey(playerName);
        assert eventLoggers.get(playerName) != null;
        log.debug("Trying to get logger for {} in {}", playerName, eventLoggers);
        return eventLoggers.get(playerName);
    }

}
