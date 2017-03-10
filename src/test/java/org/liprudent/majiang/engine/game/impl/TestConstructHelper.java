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

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.liprudent.majiang.engine.event.KindOfWall;
import org.liprudent.majiang.engine.game.IGame;
import org.liprudent.majiang.engine.player.IPlayer;
import org.liprudent.majiang.engine.player.IPlayer.Wind;
import org.liprudent.majiang.engine.player.impl.Player;
import org.liprudent.majiang.engine.tile.ITile;
import org.liprudent.majiang.engine.tile.impl.TileHonorEast;
import org.liprudent.majiang.engine.tile.impl.TileHonorGreen;
import org.liprudent.majiang.engine.tile.impl.TileHonorNorth;
import org.liprudent.majiang.engine.tile.impl.TileHonorRed;
import org.liprudent.majiang.engine.tile.impl.TileHonorSouth;
import org.liprudent.majiang.engine.tile.impl.TileHonorWest;
import org.liprudent.majiang.engine.tile.impl.TileHonorWhite;
import org.liprudent.majiang.engine.tile.impl.TileSuitBamboo;
import org.liprudent.majiang.engine.tile.impl.TileSuitCharacter;
import org.liprudent.majiang.engine.tile.impl.TileSuitDot;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public abstract class TestConstructHelper {
	public static final String PLAYER_4 = "Player 4";
	public static final String PLAYER_3 = "Player 3";
	public static final String PLAYER_2 = "Player 2";
	public static final String PLAYER_1 = "Player 1";

	/**
	 * Construct Collection of tiles from string A tile is defined by a string :
	 * first char is the number (only available for bamboos, charaters and
	 * stones), second is the kind of tile (b=bamboo,
	 * s=stone,c=character,N=north,S=south,E=east,W=west,r=red,g=green,w=white),
	 * third is clone number.
	 * 
	 * @param tiles
	 * @return a Collection of tiles
	 */
	public static SortedSet<ITile> set(final String tiles) {
		final SortedSet<ITile> ret = new TreeSet<ITile>();
		final String[] allTiles = tiles.split(",");
		for (final String tile : allTiles) {
			ret.add(tile(tile));
		}
		return ret;
	}

	public static ITile tile(final String tile) {
		if (tile.length() == 3) {
			if (tile.charAt(1) == 'b') {
				return new TileSuitBamboo(TestConstructHelper.n(tile, 2),
						TestConstructHelper.n(tile, 0));
			} else if (tile.charAt(1) == 'c') {
				return new TileSuitCharacter(TestConstructHelper.n(tile, 2),
						TestConstructHelper.n(tile, 0));
			} else if (tile.charAt(1) == 's') {
				return new TileSuitDot(TestConstructHelper.n(tile, 2),
						TestConstructHelper.n(tile, 0));
			} else {
				throw new IllegalArgumentException();
			}
		} else if (tile.length() == 2) {
			if (tile.charAt(0) == 'N') {
				return new TileHonorNorth(TestConstructHelper.n(tile, 1));
			} else if (tile.charAt(0) == 'E') {
				return new TileHonorEast(TestConstructHelper.n(tile, 1));
			} else if (tile.charAt(0) == 'W') {
				return new TileHonorWest(TestConstructHelper.n(tile, 1));
			} else if (tile.charAt(0) == 'S') {
				return new TileHonorSouth(TestConstructHelper.n(tile, 1));
			} else if (tile.charAt(0) == 'r') {
				return new TileHonorRed(TestConstructHelper.n(tile, 1));
			} else if (tile.charAt(0) == 'g') {
				return new TileHonorGreen(TestConstructHelper.n(tile, 1));
			} else if (tile.charAt(0) == 'w') {
				return new TileHonorWhite(TestConstructHelper.n(tile, 1));
			} else {
				throw new IllegalArgumentException();
			}
		} else {
			throw new IllegalArgumentException();
		}
	}

	private static int n(final String tile, final int n) {
		return Integer.valueOf(new String(new char[] { tile.charAt(n) }));
	}

	private static void addKongToPlayer(final IPlayer player,
			final SortedSet<ITile> kong1, final IGame game) {
		player.giveTilesInConcealedHand(kong1, KindOfWall.WALL);
		game.getLastRound().getTileSet().getWall().removeAll(kong1);
		game.getLastRound().getTileSet().getDeadWall().removeAll(kong1);
	}

	private TestConstructHelper() {
	}

	public static IGame constructGame() {
		return createGame();
	}

	private static IGame createGame() {
		return new Game(GameFactory.buildPlayersSet(Sets.newHashSet(PLAYER_1,PLAYER_2, PLAYER_3, PLAYER_4)));
	}

	public static List<? extends IPlayer> construct4Players() {
		final List<? extends IPlayer> players = Lists.newArrayList(Player
				.instanceOf(PLAYER_1), Player.instanceOf(PLAYER_2), Player
				.instanceOf(PLAYER_3), Player.instanceOf(PLAYER_4));

		return players;
	}

	/**
	 * @return A game. Current player have a natural kong 1 bamboo
	 */
	public static IGame constructSpecialKongGame() {
		final IGame game = createGame();
		final IPlayer currentPlayer = game.getLastRound().getCurrentPlayer();
		clearConcealedHand(currentPlayer);

		final SortedSet<ITile> kong1 = TestConstructHelper.kong(1);
		TestConstructHelper.addKongToPlayer(currentPlayer, kong1, game);

		currentPlayer.giveTilesInConcealedHand(game.getLastRound().getTileSet()
				.giveTilesFromWalls(10), KindOfWall.WALL);
		return game;

	}

	/**
	 * @return A game. Current player have a natural kong 1 and 2 bamboo
	 */
	public static IGame constructSpecial2KongsGame() {
		final IGame game = createGame();
		final IPlayer currentPlayer = game.getLastRound().getCurrentPlayer();
		clearConcealedHand(currentPlayer);

		final SortedSet<ITile> kong1 = TestConstructHelper.kong(1);
		TestConstructHelper.addKongToPlayer(currentPlayer, kong1, game);

		final SortedSet<ITile> kong2 = TestConstructHelper.kong(2);
		TestConstructHelper.addKongToPlayer(currentPlayer, kong2, game);

		currentPlayer.giveTilesInConcealedHand(game.getLastRound().getTileSet()
				.giveTilesFromWalls(6), KindOfWall.WALL);
		return game;
	}

	/**
	 * Construct a kong of kind TileSuitBamboo
	 * 
	 * @param number
	 *            Number
	 * @return a Kong of number TileSuitBamboo
	 */
	public static SortedSet<ITile> kong(final int number) {
		final SortedSet<ITile> kong = new TreeSet<ITile>();
		for (int clone = 0; clone < 4; clone++) {
			kong.add(new TileSuitBamboo(clone, number));
		}
		return kong;
	}

	/**
	 * @return A player chained whit 3 others(getPrevious, getNext are ok)
	 */
	public static IPlayer constructChainedPlayers() {
		final IPlayer playerE = Player.instanceOf("P1", Wind.EAST, null, null);
		final IPlayer playerN = Player.instanceOf("P2", Wind.NORTH, null,
				playerE);
		final IPlayer playerW = Player.instanceOf("P3", Wind.WEST, null,
				playerN);
		final IPlayer playerS = Player.instanceOf("P2", Wind.SOUTH, null,
				playerW);
		playerE.setNext(playerN);
		playerE.setPrevious(playerS);
		playerN.setNext(playerW);
		playerW.setNext(playerS);
		playerS.setNext(playerE);
		return playerE;
	}

	public static void constructConcealedHand(final IPlayer next,
			final String string) {
		clearConcealedHand(next);
		next.giveTilesInConcealedHand(set(string), KindOfWall.WALL);
	}

	public static void clearConcealedHand(final IPlayer next) {
		while (!next.getTiles().getConcealedHand().isEmpty()) {
			next.discardTile(next.getTiles().getConcealedHand().iterator()
					.next());
		}

	}

}
