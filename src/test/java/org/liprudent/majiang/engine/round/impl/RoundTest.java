/**
 * Majiang is a library that implements Mahjong game rules.
 * <p>
 * Copyright 2009 Prudent Jérome
 * <p>
 * This file is part of Majiang.
 * <p>
 * Majiang is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * Majiang is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * <p>
 * You can contact me at jprudent@gmail.com
 */
package org.liprudent.majiang.engine.round.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Iterator;
import java.util.SortedSet;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
import org.junit.Test;
import org.liprudent.majiang.engine.event.KindOfWall;
import org.liprudent.majiang.engine.game.impl.TestConstructHelper;
import org.liprudent.majiang.engine.player.IPlayer;
import org.liprudent.majiang.engine.round.Action;
import org.liprudent.majiang.engine.round.IRound;
import org.liprudent.majiang.engine.round.impl.Round;
import org.liprudent.majiang.engine.round.State;
import org.liprudent.majiang.engine.tile.ITile;
import org.liprudent.majiang.engine.tile.TileFamily;
import org.liprudent.majiang.engine.tile.impl.TileSuitCharacter;
import org.liprudent.majiang.engine.tile.impl.TileSuitDot;

public class RoundTest {
    private static final SortedSet<ITile> THROW_WEST_HAND = TestConstructHelper
            .set("1c0,2c0,3c0,4c0,5c0,6c0,7c0,8c0,9c0,1c1,2c1,3c1,4c1,W1");

    @Test
    public void constructor() {
        final IPlayer playerE = TestConstructHelper.constructChainedPlayers();
        final IPlayer playerN = playerE.getNext();
        final IPlayer playerW = playerN.getNext();
        final IPlayer playerS = playerW.getNext();

        final ITile dummyTile = new ITile() {

            @Override
            public int getId() {
                return 0;
            }

            @Override
            public TileFamily getFamily() {
                return null;
            }

            @Override
            public String getUniqueId() {
                return null;
            }

            @Override
            public boolean compare(ITile obj) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public int getCloneValue() {
                // TODO Auto-generated method stub
                return 0;
            }

            @Override
            public int compareTo(ITile o) {
                // TODO Auto-generated method stub
                return 0;
            }
        };
        // add tiles to a player (constructor must init player hand)
        playerE.giveTileInConcealedHand(dummyTile, KindOfWall.WALL);
        playerN.giveTileInConcealedHand(dummyTile, KindOfWall.WALL);
        playerW.giveTileInConcealedHand(dummyTile, KindOfWall.WALL);
        playerS.giveTileInConcealedHand(dummyTile, KindOfWall.WALL);

        new Round(playerE);

        Assert.assertEquals(14, playerE.getTiles().getConcealedHand().size());
        Assert.assertEquals(13, playerN.getTiles().getConcealedHand().size());
        Assert.assertEquals(13, playerS.getTiles().getConcealedHand().size());
        Assert.assertEquals(13, playerW.getTiles().getConcealedHand().size());

        Assert.assertEquals(State.HAVE_A_NEW_TILE, playerE.getState());
        Assert.assertEquals(State.WAIT_TILE_THROWED, playerN.getState());
        Assert.assertEquals(State.WAIT_TILE_THROWED, playerS.getState());
        Assert.assertEquals(State.WAIT_TILE_THROWED, playerW.getState());

    }

    @Test
    public void handlePlayer1Tile() {
        final IRound round = TestConstructHelper.constructGame().getLastRound();
        final ITile discardedTile = round.getCurrentPlayer().getTiles().getConcealedHand().iterator().next();

        round.handlePlayer(round.getCurrentPlayer(), Action.DISCARD_TILE, discardedTile);

        // assert that treatment has been performed
        assertFalse(round.getCurrentPlayer().getTiles().getConcealedHand().contains(discardedTile));
        // assert that chang state has been performed
        assertEquals(State.END, round.getCurrentPlayer().getState());
    }

    @Test
    public void handlePlayerSeveralTiles() {
        final IRound round = TestConstructHelper.constructSpecialKongGame().getLastRound();
        round.handlePlayer(round.getCurrentPlayer(), Action.CONCEALED_KONG, TestConstructHelper.kong(1));
        // assert that treatment has been performed
        assertFalse(CollectionUtils.containsAny(round.getCurrentPlayer().getTiles().getConcealedHand(), TestConstructHelper.kong(1)));
    }

    @Test
    public void handlePlayerTreatmentInvalidButCanPlayAgainBecauseHeIsCurrent() {
        final IRound round = TestConstructHelper.constructGame().getLastRound();

        // treatment is invalid because he does not own the tile (it comes from
        // wall)
        final ITile badDiscardedTile = round.getTileSet().getWall().get(0);
        final IPlayer current = round.getCurrentPlayer();
        round.handlePlayer(current, Action.DISCARD_TILE, badDiscardedTile);
        // assert that change state has not been performed
        assertEquals(State.HAVE_A_NEW_TILE, current.getState());

        // then he can replay
        final ITile goodDiscardedTile = current.getTiles().getConcealedHand().iterator()
                .next();
        round.handlePlayer(current, Action.DISCARD_TILE, goodDiscardedTile);
        // assert that change state has not been performed
        assertDiscardTileHasBeenPerformed(current, round);

        allPass(current, round);

        // assert the turn end
        assertTurnEnd(current, current.getNext(), round);
    }

    private static void assertDiscardTileHasBeenPerformed(
            final IPlayer current, final IRound round) {
        assertEquals(State.END, current.getState());
        for (IPlayer next = current.getNext(); next != current; next = next
                .getNext()) {
            assertEquals(State.CHOOSE_ACTION, next.getState());
        }
    }

    private static void assertTurnEnd(final IPlayer lastCurrent,
                                      final IPlayer newCurrent,
                                      final IRound round) {
        assertEquals(newCurrent, round.getCurrentPlayer());
        assertEquals(State.HAVE_A_NEW_TILE, newCurrent.getState());
        assertEquals(State.WAIT_TILE_THROWED, newCurrent.getNext().getState());
        assertEquals(State.WAIT_TILE_THROWED, newCurrent.getNext().getNext()
                .getState());
        assertEquals(State.WAIT_TILE_THROWED, newCurrent.getNext().getNext()
                .getNext().getState());
        for (IPlayer next = newCurrent.getNext(); next != newCurrent; next = next
                .getNext()) {
            assertEquals(State.WAIT_TILE_THROWED, next.getState());
        }
    }

    private void allPass(final IPlayer current, final IRound round) {
        for (IPlayer next = current.getNext(); next != current; next = next
                .getNext()) {
            round.handlePlayer(next, Action.PASS);
            if (next.getNext() != current) {
                assertEquals(State.END, next.getState());
            }
        }

    }

	/*
     * If all players are in state WAIT_OTHERS_ACTION or END, elect an action
	 */

    /**
     *
     */
    @Test
    public void handlePlayerElectAction() {
        final IRound round = TestConstructHelper.constructGame().getLastRound();
        // current player throw a West
        final IPlayer current = round.getCurrentPlayer();
        TestConstructHelper.clearConcealedHand(current);
        current.giveTilesInConcealedHand(THROW_WEST_HAND, KindOfWall.WALL);
        final TileSuitCharacter discardedTile = new TileSuitCharacter(1, 1);
        round.handlePlayer(current, Action.DISCARD_TILE, discardedTile);
        assertEquals(State.END, current.getState());

        // this player chow
        final SortedSet<ITile> nearlyMahjong = TestConstructHelper
                .set("2c1,3c1,S0,S1,S2,E0,E1,E2,r0,r1,r2,W0");
        final IPlayer playerCanChow = round.getCurrentPlayer().getNext();
        TestConstructHelper.clearConcealedHand(playerCanChow);
        playerCanChow.giveTilesInConcealedHand(nearlyMahjong, KindOfWall.WALL);
        round.handlePlayer(playerCanChow, Action.CHOW,
                TestConstructHelper.set("2c1,3c1"));
        assertEquals(State.WAIT_OTHERS_ACTION, playerCanChow.getState());

        // this player pung
        final SortedSet<ITile> canPung = TestConstructHelper
                .set("1b0,2b0,3b0,4b0,5b0,6b0,7b0,8b0,9b0,9b1,9b2,1c2,1c3");
        final IPlayer playerCanPung = playerCanChow.getNext();
        TestConstructHelper.clearConcealedHand(playerCanPung);
        playerCanPung.giveTilesInConcealedHand(canPung, KindOfWall.WALL);
        round.handlePlayer(playerCanPung, Action.PONG);
        assertEquals(State.WAIT_OTHERS_ACTION, playerCanPung.getState());

        // this player pass
        final IPlayer playerPass = current.getPrevious();
        round.handlePlayer(playerPass, Action.PASS);

		/*
         * ASSERTIONS
		 */

        // assert playerCanPung have tiles in opened hand
        assertEquals(1, playerCanPung.getTiles().getOpenedHand().size());
        final Collection<ITile> pungInOpenedHand = playerCanPung
                .getTiles().getOpenedHand().iterator().next();
        assertTrue(pungInOpenedHand.containsAll(TestConstructHelper
                .set("1c1,1c3,1c2")));

        // assert discarded tile is not in discarded tiles
        assertFalse(round.getTileSet().getDiscardedTiles().contains(
                discardedTile));

        // assert playerCanPung is now current player
        assertEquals(round.getCurrentPlayer(), playerCanPung);

        // now assert that a new turn init
        assertEquals(State.HAVE_A_NEW_TILE, playerCanPung.getState());
        System.out.println(playerPass.getState());
        assertEquals(State.WAIT_TILE_THROWED, playerPass.getState());
        System.out.println(current.getState());
        assertEquals(State.WAIT_TILE_THROWED, current.getState());
        System.out.println(playerCanChow.getState());
        assertEquals(State.WAIT_TILE_THROWED, playerCanChow.getState());
    }

    @Test
    public void handlePlayerAllPass() {
        final IRound round = TestConstructHelper.constructGame().getLastRound();
        final IPlayer current = round.getCurrentPlayer();
        final ITile discardedTile = current.getTiles().getConcealedHand().iterator()
                .next();
        round.handlePlayer(current, Action.DISCARD_TILE, discardedTile);
        assertEquals(State.END, current.getState());

        // this player pass
        final IPlayer player2 = round.getCurrentPlayer().getNext();
        round.handlePlayer(player2, Action.PASS);
        assertEquals(State.END, player2.getState());

        // this player pass
        final IPlayer player3 = player2.getNext();
        round.handlePlayer(player3, Action.PASS);
        assertEquals(State.END, player3.getState());

        // this player pass
        final IPlayer player4 = player3.getNext();
        round.handlePlayer(player4, Action.PASS);

		/*
         * ASSERTIONS
		 */

        // assert discarded tile is in discarded tiles
        assertTrue(round.getTileSet().getDiscardedTiles().contains(
                discardedTile));

        // assert player2 is now current player
        assertEquals(round.getCurrentPlayer(), player2);

        // now assert that a new turn init
        assertEquals(State.HAVE_A_NEW_TILE, player2.getState());
        assertEquals(State.WAIT_TILE_THROWED, player3.getState());
        assertEquals(State.WAIT_TILE_THROWED, player4.getState());
        assertEquals(State.WAIT_TILE_THROWED, current.getState());
    }

    @Test
    public void handlePlayerDeclareMahjong() {
        final IRound round = TestConstructHelper.constructGame().getLastRound();
        // current player throw a West
        final IPlayer current = round.getCurrentPlayer();
        TestConstructHelper.clearConcealedHand(current);
        current
                .giveTilesInConcealedHand(
                        TestConstructHelper
                                .set("1c0,2c0,3c0,4c0,5c0,6c0,7c0,8c0,9c0,1c1,2c1,3c1,4c1,4c2"),
                        KindOfWall.WALL);
        round.handlePlayer(current, Action.DECLARE_MAHJONG);

        assertTrue(((Round) round).isEnd());
    }

    @Test
    public void handlePlayerDoubleMahjong() {
        final IRound round = TestConstructHelper.constructGame().getLastRound();
        // current player throw a West
        final IPlayer current = round.getCurrentPlayer();
        TestConstructHelper.clearConcealedHand(current);
        current
                .giveTilesInConcealedHand(
                        TestConstructHelper
                                .set("1c0,2c0,3c0,4c0,5c0,6c0,7c0,8c0,9c0,1c1,2c1,3c1,4c1,1s0"),
                        KindOfWall.WALL);

        //NEXT.NEXT configuration
        final SortedSet<ITile> nearlyMahjong2 = TestConstructHelper
                .set("2s1,3s1,4s1,5s1,6s1,7s1,8s1,9s1,1b1,2b1,3b1,4b2,4b3");
        final IPlayer playerCanMahjong2 = current.getNext().getNext();
        TestConstructHelper.clearConcealedHand(playerCanMahjong2);
        playerCanMahjong2.giveTilesInConcealedHand(nearlyMahjong2,
                KindOfWall.WALL);

//NEXT configuration
        final SortedSet<ITile> nearlyMahjong1 = TestConstructHelper
                .set("2s0,3s0,4s0,5s0,6s0,7s0,8s0,9s0,1b0,2b0,3b0,4b0,4b1");
        final IPlayer playerCanMahjong1 = round.getCurrentPlayer().getNext();
        TestConstructHelper.clearConcealedHand(playerCanMahjong1);
        playerCanMahjong1.giveTilesInConcealedHand(nearlyMahjong1,
                KindOfWall.WALL);


        //current player discard
        final ITile discardedTile = new TileSuitDot(0, 1);
        round.handlePlayer(current, Action.DISCARD_TILE, discardedTile);
        assertEquals(State.END, current.getState());

        // this player mahjong
        round.handlePlayer(playerCanMahjong2, Action.ACTION_MAHJONG);
        assertEquals(State.WAIT_OTHERS_ACTION, playerCanMahjong2.getState());
        // assert discarded tile is STILL in discarded tiles
        assertTrue(round.getTileSet().getDiscardedTiles().contains(
                discardedTile));

        // this player mahjong
        round.handlePlayer(playerCanMahjong1, Action.ACTION_MAHJONG);
        assertEquals(State.WAIT_OTHERS_ACTION, playerCanMahjong1.getState());

        // this player pass
        final IPlayer playerPass = current.getPrevious();
        round.handlePlayer(playerPass, Action.PASS);

		/*
		 * ASSERTIONS
		 */

        // assert discarded tile is not in discarded tiles
        assertFalse(round.getTileSet().getDiscardedTiles().contains(
                discardedTile));

        assertTrue(((Round) round).isEnd());
        assertEquals(playerCanMahjong1, round.getWinner());
    }

    @Test
    public void handlePlayerCantEatTwiceIfError() {
        final IRound round = TestConstructHelper.constructGame().getLastRound();
        // current player throw a West
        final IPlayer current = round.getCurrentPlayer();
        TestConstructHelper.clearConcealedHand(current);
        current.giveTilesInConcealedHand(THROW_WEST_HAND, KindOfWall.WALL);
        final TileSuitCharacter discardedTile = new TileSuitCharacter(1, 1);
        round.handlePlayer(current, Action.DISCARD_TILE, discardedTile);
        assertEquals(State.END, current.getState());

        // this player can chow but he pung
        final SortedSet<ITile> nearlyMahjong = TestConstructHelper
                .set("2c1,3c1,S0,S1,S2,E0,E1,E2,r0,r1,r2,W0");
        final IPlayer playerCanChow = round.getCurrentPlayer().getNext();
        TestConstructHelper.clearConcealedHand(playerCanChow);
        playerCanChow.giveTilesInConcealedHand(nearlyMahjong, KindOfWall.WALL);
        round.handlePlayer(playerCanChow, Action.PONG);
        assertEquals(State.END, playerCanChow.getState());

        // then others can play

    }


    private static class TestableRoundNoWinner extends Round {

        public TestableRoundNoWinner(final IPlayer currentPlayer) {
            super(currentPlayer);
        }

        @Override
        public IPlayer getWinner() {
            return null;
        }

        @Override
        public boolean isEnd() {
            return true;
        }

    }

    @Test
    public void playerStateChangeWhenEndNowinner() {
        final IPlayer player = TestConstructHelper.constructChainedPlayers();
        new TestableRoundNoWinner(player).changePlayerStateWhenRoundEnd();
        for (final IPlayer p : player) {
            assertEquals(State.ROUND_LOOSER, p.getState());
        }
    }

    private static class TestableRoundWinner extends Round {

        private final IPlayer winner;

        public TestableRoundWinner(final IPlayer currentPlayer) {
            super(currentPlayer);
            winner = currentPlayer;
        }

        @Override
        public IPlayer getWinner() {
            return winner;
        }

        @Override
        public boolean isEnd() {
            return true;
        }

    }

    @Test
    public void playerStateChangeWhenEndWinner() {
        final IPlayer player = TestConstructHelper.constructChainedPlayers();
        new TestableRoundWinner(player).changePlayerStateWhenRoundEnd();
        assertEquals(State.ROUND_WINNER, player.getState());
        final Iterator<IPlayer> it = player.othersIterator();
        while (it.hasNext()) {
            assertEquals(State.ROUND_LOOSER, it.next().getState());
        }
    }

}
