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
package org.liprudent.majiang.engine.player.impl;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.liprudent.majiang.engine.event.GameInfo;
import org.liprudent.majiang.engine.event.IEventObserver;
import org.liprudent.majiang.engine.event.KindOfHand;
import org.liprudent.majiang.engine.event.KindOfWall;
import org.liprudent.majiang.engine.event.impl.EventFactory;
import org.liprudent.majiang.engine.player.IPlayer;
import org.liprudent.majiang.engine.player.IPlayer.Wind;
import org.liprudent.majiang.engine.player.IPlayerTiles;
import org.liprudent.majiang.engine.round.State;
import org.liprudent.majiang.engine.tile.ITile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.google.common.collect.UnmodifiableIterator;

public class Player extends Observable implements IPlayer {

	private Wind wind;
	private IPlayer next;
	private IPlayer previous;
	private State state;
	private IPlayerTiles playerTiles;
	private IPlayerTilesActions playerTilesActions;
	private final String name;
	private final Set<Observer> observers;
	private final Logger log = LoggerFactory.getLogger(Player.class);

	public static Player instanceOf(final String name) {
		return new Player(name, null, null, null, new PlayerTiles());
	}

	public static Player instanceOf(final String name, final Wind wind,
			final IPlayer previous, final IPlayer next) {
		return new Player(name, wind, next, previous, new PlayerTiles());
	}

	public static IPlayer instanceOf(final String name, final Wind wind) {
		return new Player(name, wind, null, null, new PlayerTiles());
	}

	private Player(final String name, final Wind wind, final IPlayer next,
			final IPlayer previous, final PlayerTiles playerTiles) {
		super();
		this.name = name(name);
		setWind(wind);
		this.next = next;
		this.previous = previous;
		this.playerTiles = playerTiles;
		this.playerTilesActions = playerTiles;
		this.observers = new HashSet<Observer>();
	}

	private String name(final String name) {
		if (StringUtils.isBlank(name)) {
			throw new NullPointerException("name is empty");
		}
		return name;
	}

	public Wind getWind() {
		return wind;
	}

	public void setWind(final Wind wind) {
		this.wind = wind;
		setChanged();
		notifyObservers(EventFactory.instanceOf(name, wind));
	}

	public IPlayer getNext() {
		return next;
	}

	public void setNext(final IPlayer next) {
		this.next = next;
	}

	public void setPrevious(final IPlayer previous) {
		this.previous = previous;
	}

	public IPlayer getPrevious() {
		return previous;
	}

	@Override
	public State getState() {
		return state;
	}

	@Override
	public void setState(final State state) {
		Preconditions.checkNotNull(state);
		Preconditions.checkArgument(state != this.state,
				"Player's state is already " + state);
		this.state = state;
		setChanged();
		notifyObservers(EventFactory.instanceOf(name, wind, state));
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Iterator<IPlayer> iterator() {
		return new PlayerIterator(this);
	}

	private static abstract class AbstractPlayerIterator extends
			UnmodifiableIterator<IPlayer> {
		private final IPlayer firstPlayer;
		private IPlayer current;
		private Boolean hasNext;

		public AbstractPlayerIterator(final IPlayer firstPlayer,
				final IPlayer current) {
			super();
			this.firstPlayer = firstPlayer;
			this.current = current;
			hasNext = true;
		}

		@Override
		public boolean hasNext() {
			return hasNext;
		}

		@Override
		public IPlayer next() {
			final IPlayer ret = current;
			current = current.getNext();
			if (current == firstPlayer) {
				hasNext = false;
			}
			return ret;
		}

	}

	private static class PlayerIterator extends AbstractPlayerIterator {

		public PlayerIterator(final IPlayer firstPlayer) {
			super(firstPlayer, firstPlayer);
		}
	}

	private static class OtherPlayerIterator extends AbstractPlayerIterator {

		public OtherPlayerIterator(final IPlayer firstPlayer) {
			super(firstPlayer, firstPlayer.getNext());
		}

	}

	@Override
	public Iterator<IPlayer> othersIterator() {
		return new OtherPlayerIterator(this);
	}

	// FIXME use HashcodeBuilder
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (name == null ? 0 : name.hashCode());
		return result;
	}

	// FIXME use EqualsBuilder
	/**
	 * Two players are equal if they do have the same name
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Player other = (Player) obj;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}

	// FIXME Unit test
	@Override
	public void moveFromConcealedToOpen(final Collection<ITile> tiles) {
		playerTilesActions.moveFromConcealedToOpen(tiles);
		setChanged();
		notifyObservers(EventFactory.instanceOf(name, wind, tiles,
				KindOfHand.OPENED));
	}

	// FIXME Unit test
	@Override
	public void discardTile(final ITile throwedTile) {
		playerTilesActions.discardTile(throwedTile);
		setChanged();
		notifyObservers(EventFactory.instanceOf(name, wind, throwedTile));
	}

	@Override
	public void giveTilesInConcealedHand(final SortedSet<ITile> tiles,
			final KindOfWall from) {
		playerTilesActions.giveTilesInConcealedHand(tiles);
		setChanged();
		notifyObservers(EventFactory.instanceOf(name, wind, tiles, from,
				KindOfHand.CONCEALED));
	}

	@Override
	public void giveTileInConcealedHand(final ITile tile, final KindOfWall from) {
		playerTilesActions.giveTileInConcealedHand(tile);
		setChanged();
		notifyObservers(EventFactory.instanceOf(name, wind, tile, from,
				KindOfHand.CONCEALED));
	}

	// FIXME Unit test
	@Override
	public void moveFromConcealedToHidden(final Collection<ITile> tiles) {
		playerTilesActions.moveFromConcealedToHidden(tiles);
		setChanged();
		notifyObservers(EventFactory.instanceOf(name, wind, tiles,
				KindOfHand.HIDDEN));

	}

	// TODO Unit test
	@Override
	public void moveFromConcealedToOpen(final ITile tile,
			final Collection<ITile> pung) {
		//REFACTORING: We define this variable becase "pung" get modified in moveFromConcealedToOpen
		final TreeSet<ITile> originalpung = Sets.newTreeSet(pung);
		
		playerTilesActions.moveFromConcealedToOpen(tile, pung);
		setChanged();
		notifyObservers(EventFactory.instanceOf(name, wind, tile, originalpung));

	}

	@Override
	public void clearAllTiles() {
		PlayerTiles pTiles = new PlayerTiles();
		playerTiles = pTiles;
		playerTilesActions = pTiles;

	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append(name).append(wind)
				.append(state).append(playerTiles).toString();
	}

	@Override
	public void joinGame(GameInfo gameInfo) {
		setChanged();
		notifyObservers(EventFactory.instanceOf(name, gameInfo));
	}

	@Override
	public IPlayerTiles getTiles() {
		return playerTiles;
	}

	@Override
	public void moveDiscardedTileInOpen(ITile tile,
			SortedSet<ITile> tilesInOpenHand, Wind discarder) {
		playerTilesActions.moveTileInOpen(tile, tilesInOpenHand);
		setChanged();
		notifyObservers(EventFactory.instanceOf(name, wind, tile, discarder,
				tilesInOpenHand));
	}

	@Override
	/**
	 * This method is overriden in order to populate our own list of observers
	 */
	public synchronized void addObserver(Observer o) {
		super.addObserver(o);
		if (o instanceof IEventObserver) {
			observers.add(o);
		}
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		in.defaultReadObject();
		// add the observers
		for (Observer obs : observers) {
			addObserver(obs);
		}
	}

}
