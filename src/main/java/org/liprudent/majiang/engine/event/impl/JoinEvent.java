package org.liprudent.majiang.engine.event.impl;

import org.liprudent.majiang.engine.event.GameInfo;
import org.liprudent.majiang.engine.event.IEvent;
import org.liprudent.majiang.engine.event.IJoinEvent;
import org.liprudent.majiang.engine.event.NoBroadcastEvent;
import org.liprudent.majiang.engine.player.IPlayer.Wind;

public class JoinEvent extends BasicEvent implements IJoinEvent,NoBroadcastEvent {

	final private GameInfo gameInfo;
	final KindOfEvent kind = KindOfEvent.GAME_OPTIONS;
	public JoinEvent(String name, Wind wind, GameInfo gameInfo) {
		super(name, wind);
		this.gameInfo = gameInfo;
	}

	@Override
	public IEvent publicView() {
		throw new IllegalStateException("publicView shouldn't be called");
	}

	@Override
	public GameInfo getGameInfo() {
		return gameInfo;
	}
	@Override
	public KindOfEvent getKind() {
		return kind;
	}
}
