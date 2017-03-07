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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import org.liprudent.majiang.engine.game.impl.CrookedGame;
import org.liprudent.majiang.engine.event.GameInfo;
import org.liprudent.majiang.engine.event.IEventLogger;
import org.liprudent.majiang.engine.event.impl.EndRoundDrawEvent;
import org.liprudent.majiang.engine.event.impl.EndRoundEvent;
import org.liprudent.majiang.engine.event.impl.EventObserver;
import org.liprudent.majiang.engine.event.impl.GameOverEvent;
import org.liprudent.majiang.engine.game.IGame;
import org.liprudent.majiang.engine.game.IGameClient;
import org.liprudent.majiang.engine.player.IPlayer;
import org.liprudent.majiang.engine.player.IPlayer.Wind;
import org.liprudent.majiang.engine.player.impl.Player;
import org.liprudent.majiang.engine.round.Action;
import org.liprudent.majiang.engine.round.IRound;
import org.liprudent.majiang.engine.round.impl.Round;
import org.liprudent.majiang.engine.round.Scoring;
import org.liprudent.majiang.engine.tile.ITile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import scala.collection.JavaConversions;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

public class GameFactory {
	public static IGameClient instanceOf(final Set<String> playerNames) {
		checkPlayersArg(playerNames);
		final Set<IPlayer> players = buildPlayersSet(playerNames);
		return new Game(players);
	}

	public static IGameClient instanceOf(final Set<String> playerNames,
			scala.collection.immutable.Map<String, IPlayer.Wind> crookedWinds,
			scala.collection.immutable.List<ITile> crookedWall) {
		checkPlayersArg(playerNames);
		final Set<IPlayer> players = buildPlayersSet(playerNames);
		for (IPlayer p : players) {
			p.setWind(crookedWinds.get(p.getName()).get());
		}
		return new CrookedGame(players, crookedWall);
	}

	protected static Set<IPlayer> buildPlayersSet(final Set<String> playerNames) {

		final Set<IPlayer> players = new HashSet<IPlayer>(4);
		for (final String name : playerNames) {
			players.add(Player.instanceOf(name));
		}
		return players;
	}

	private static void checkPlayersArg(final Set<String> playerNames) {
		if (playerNames.size() != 4) {
			throw new IllegalArgumentException(
					"There is only 4 players allowed");
		}
	}
}

class Game implements IGame, IGameClient, Observer {
	private Logger log = LoggerFactory.getLogger(Game.class);
	// OPTIMIZE: use a map with name<->obj to optimize
	private final Set<IPlayer> players;
	private final List<IRound> rounds;
	private EventObserver eventsObserver;

	// the current dealer (used to instantiate a round)
	private IPlayer dealer;

	// TODO this constructor does too much? create an init method?
	protected Game(final Set<IPlayer> players) {
		if (players.size() != 4) {
			throw new IllegalArgumentException(
					"There is only 4 players allowed");
		}

		this.players = Collections.unmodifiableSet(players);

		// create an observer
		eventsObserver = new EventObserver(players);
		GameInfo gameInfo = new GameInfo(new Date(), 136, 123, 13, false);
		// add this observer to each player
		for (final IPlayer player : players) {
			player.addObserver(eventsObserver);
			player.joinGame(gameInfo);
		}

		// give a wind
		dealer = chooseWindAndLink();

		// initialize rounds
		rounds = new ArrayList<IRound>(4);
		rounds.add(instanciateRound(dealer));

	}

	protected IRound instanciateRound(final IPlayer firstPlayer) {
		final Round round = new Round(firstPlayer, this);
		round.addObserver(this);
		return round;
	}

	/**
	 * Randomly choose wind for each player and link them (previous, next)
	 * 
	 * @return A pointer to the first to begin
	 */
	protected IPlayer chooseWindAndLink() {
		final List<IPlayer> randomPlayers = new ArrayList<IPlayer>(players);
		Collections.shuffle(randomPlayers);

		for (int i = 0; i < 4; i++) {
			final IPlayer player = randomPlayers.get(i);

			// set next and previous player
			final IPlayer playerNext = randomPlayers.get((i + 1) % 4);
			player.setNext(playerNext);
			playerNext.setPrevious(player);

			// set wind
			givePlayerWind(i, player);
		}
		return randomPlayers.get(0);
	}

	private void givePlayerWind(final int i, final IPlayer player) {
		// give wind
		final Wind wind = Wind.values()[i];
		player.setWind(wind);

	}

	@Override
	public IRound getLastRound() {
		return rounds.get(rounds.size() - 1);
	}

	@Override
	public IEventLogger getEvents(final String playerName) {
		Preconditions.checkArgument(players.contains(Player
				.instanceOf(playerName)));

		fixmeAddGameObserverToRound();

		return eventsObserver.eventLogger(playerName);
	}

	/**
	 * Delegate to lastRound
	 * 
	 * @see org.liprudent.majiang.engine.game.IGameAction#handlePlayer(org.liprudent.majiang.engine.player.IPlayer,
	 *      org.liprudent.majiang.engine.round.Action,
	 *      org.liprudent.majiang.engine.tile.ITile)
	 */
	@Override
	public boolean handlePlayer(final String player, final Action action,
			final ITile tile) {
		fixmeAddGameObserverToRound();

		return getLastRound().handlePlayer(findPlayer(player), action, tile);

	}

	/**
	 * Delegate to lastRound
	 * 
	 * @see org.liprudent.majiang.engine.game.IGameAction#handlePlayer(org.liprudent.majiang.engine.player.IPlayer,
	 *      org.liprudent.majiang.engine.round.Action,
	 *      org.liprudent.majiang.engine.tile.ITile)
	 */
	@Override
	public boolean handlePlayer(final String player, final Action action,
			final Collection<ITile> tiles) {
		fixmeAddGameObserverToRound();

		return getLastRound().handlePlayer(findPlayer(player), action, tiles);
	}

	/**
	 * Delegate to lastRound
	 * 
	 * @see org.liprudent.majiang.engine.game.IGameAction#handlePlayer(org.liprudent.majiang.engine.player.IPlayer,
	 *      org.liprudent.majiang.engine.round.Action,
	 *      org.liprudent.majiang.engine.tile.ITile)
	 */
	@Override
	public boolean handlePlayer(final String player, final Action action) {

		fixmeAddGameObserverToRound();
		return getLastRound().handlePlayer(findPlayer(player), action);
	}

	public void fixmeAddGameObserverToRound() {
		// FIXME serialization issue with Play!
		// can't add the observer in writeObject of Round
		((Observable) getLastRound()).addObserver(this);
	}

	public IPlayer findPlayer(final String player) {
		return Iterables.find(players, new Predicate<IPlayer>() {
			@Override
			public boolean apply(final IPlayer input) {
				return input.getName().equals(player);
			}
		});
	}

	@Override
	public void update(Observable observable, Object arg) {
		if (!(observable instanceof IRound)) {
			throw new IllegalArgumentException("I can only observe IRound");
		}

		IRound round = (IRound) observable;
		if (round != getLastRound()) {
			throw new IllegalArgumentException("I can only observe last round");
		}

		if (getLastRound().getWinner() != null) {
			// create end round event
			IPlayer winner = getLastRound().getWinner();
			Scoring scoring = getLastRound().getScoring();
			log.debug("sending EndRoundEvent to " + winner);
			eventsObserver.update((Observable) winner, new EndRoundEvent(winner
					.getName(), winner.getWind(), scoring, winner.getTiles()));
		} else {
			log.debug("sending EndRoundDrawEvent");
			for (IPlayer p : players) {
				eventsObserver.update((Observable) p, new EndRoundDrawEvent(p
						.getName(), p.getWind()));
			}
		}

		// update dealer
		dealer = dealer.getNext();
		
		//instanciate round or send game over
		if (rounds.size() < 4) {
			rounds.add(instanciateRound(dealer));
		} else {
			sendGameOverEvent();
		}
	}
	
	private void sendGameOverEvent(){
		log.debug("sending GameOverEvent");
		for (IPlayer p : players) {
			eventsObserver.update((Observable) p, new GameOverEvent(p
					.getName(), p.getWind()));
		}
	}
	// private void readObject(java.io.ObjectInputStream ois)
	// throws ClassNotFoundException, java.io.IOException {
	// for (IRound round : rounds) {
	// if (((java.util.Observable)round).countObservers() == 0) {
	// ((java.util.Observable)round).addObserver(this);
	// }
	// }
	// }

}
