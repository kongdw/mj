package org.liprudent.majiang.engine.round.impl;

import com.google.common.base.Preconditions;
import org.liprudent.majiang.engine.event.KindOfWall;
import org.liprudent.majiang.engine.player.IPlayer;
import org.liprudent.majiang.engine.round.Action;
import org.liprudent.majiang.engine.round.IRound;
import org.liprudent.majiang.engine.round.Scoring;
import org.liprudent.majiang.engine.round.State;
import org.liprudent.majiang.engine.round.impl.treatment.ITreatment;
import org.liprudent.majiang.engine.round.impl.treatment.TreatmentFactory;
import org.liprudent.majiang.engine.tile.ITile;
import org.liprudent.majiang.engine.tile.ITileSets;
import org.liprudent.majiang.engine.tile.impl.TileSets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author David Kong
 * @date 2017/3/15
 */
public class Round  extends Observable implements IRound {

    private static Logger log = LoggerFactory.getLogger("Round");

    private List<IPlayer> allPlayers = new ArrayList<>();

    private List<ITurn> turns = new ArrayList<>();

    private Turn currentTurn;

    private IPlayer eastPlayer;

    private final ITileSets tileSets = new TileSets();

    public Round(IPlayer starter) {
        this(starter, null);
    }


    public Round(IPlayer starter, Observer gameObserver) {
        this.eastPlayer = starter;
        this.allPlayers.add(starter);
        this.allPlayers.add(starter.getNext());
        this.allPlayers.add(starter.getNext().getNext());
        this.allPlayers.add(starter.getNext().getNext().getNext());
        initRound();
    }

    protected void initRound() {
        // TODO do the round have to deal with this? Must be an assertion ...
        for (IPlayer player : allPlayers) {
            player.clearAllTiles();
        }
        givePlayersFirstHand();
        ITile extraTile = tileSets.giveTilesFromWalls(1).iterator().next();
        createNewTurn(eastPlayer, extraTile, KindOfWall.WALL);

    }

    protected void createNewTurn(IPlayer currentPlayer, ITile extraTile, KindOfWall source) {
        currentTurn = new Turn(currentPlayer, extraTile, source);
        turns.add(currentTurn);
    }

    /**
     * Give initial tiles in player's hand
     */
    private void givePlayersFirstHand() {
        for (IPlayer player : allPlayers) {
            addTilesToPlayer(player, 13);
        }
    }

    private void addTilesToPlayer(IPlayer player, int nbTiles) {
        SortedSet<ITile> tiles = tileSets.giveTilesFromWalls(nbTiles);
        log.debug("giving " + tiles + " to " + player);
        player.giveTilesInConcealedHand(tiles, KindOfWall.WALL);
    }

    @Override
    public Scoring getScoring() {
        return currentTurn.getScoring();
    }

    @Override
    public List<ITile> getVisibleTiles() {
        List<ITile> visibles = new ArrayList<>();

        //add discarded
        visibles = tileSets.getDiscardedTiles();

        //add all opened
        for (IPlayer p : allPlayers) {
            for (SortedSet<ITile> openTiles : p.getTiles().getOpenedHand()) {
                visibles.addAll(openTiles);
            }
        }
        return visibles;
    }

    @Override
    public IPlayer getCurrentPlayer() {
        return currentTurn.currentPlayer;
    }

    @Override
    public List<IPlayer> getOtherPlayers() {
        return currentTurn.actionPlayers;
    }

    @Override
    public ITileSets getTileSet() {
        return tileSets;
    }

    @Override
    public boolean doEnd() {
        boolean ret = isEnd();
        log.debug("doEnd - isEnd = " + ret);
        if (ret) {
            changePlayerStateWhenRoundEnd();
            log.debug("doEnd - notify the game that round ends");
            setChanged();
            notifyObservers(null);
        }
        return ret;
    }

    public boolean isEnd() {
        boolean emptyWall = tileSets.getWall().isEmpty();
        if (emptyWall)
            log.debug("Wall is empty");
        else
            log.debug("Wall is not empty");
        return currentTurn.endRoundBecauseMahjong() || (emptyWall && currentTurn.endTurn());
    }

    /**
     * This method will set ROUND_LOOSER/ROUND_WINNER to players
     */
    protected void changePlayerStateWhenRoundEnd() {
        log.debug("changePlayerStateWhenRoundEnd");
        Preconditions.checkState(isEnd(), "游戏还未结束!");
        IPlayer winner = getWinner();
        for (IPlayer player : allPlayers) {
            if (player.equals(winner)) {
                player.setState(State.ROUND_WINNER);
            } else
                player.setState(State.ROUND_LOOSER);
        }
    }

    @Override
    public IPlayer getWinner() {
        Preconditions.checkState(isEnd(), "游戏未结束！");
        if (currentTurn.endRoundBecauseMahjong())
            return currentTurn.getTurnWinner();
        else
            return null;
    }

    @Override
    public boolean handlePlayer(IPlayer player, Action action, ITile tile) {
        ITreatment treatment = TreatmentFactory.getInstance(player, action, this, tile);
        return processTreatment(treatment);
    }

    @Override
    public boolean handlePlayer(IPlayer player, Action action, Collection<ITile> tiles) {
        ITreatment treatment = TreatmentFactory.getInstance(player, action, this, tiles);
        return processTreatment(treatment);
    }

    @Override
    public boolean handlePlayer(IPlayer player, Action action) {
        ITreatment treatment = TreatmentFactory.getInstance(player, action, this);
        return processTreatment(treatment);
    }


    /**
     * Execute the treatment and modify some states if needed
     */
    private boolean processTreatment(ITreatment treatment) {
        //this following statement may change the return value of end()
        boolean valid = currentTurn.processTreatment(treatment);

        // the turn is over BUT the round is not
        if (!doEnd() && currentTurn.endTurn()) {
            initNewTurn();
            log.debug("new turn has been instantiated");
        }
        return valid;
    }

    /**
     * Change current player and player's states then instan	ciate a new turn
     */
    private void initNewTurn() {
        // set the next current player and give a tile if needed
        IPlayer currentPlayer = getNewCurrentPlayer();

        if (currentTurn.isKongTurnWinner()) {
            //since it's a kong, he get one tile from the dead wall
            createNewTurn(currentPlayer, getTileSet().giveTileFromDeadWall(), KindOfWall.DEAD_WALL);
        } else if (currentTurn.getTurnWinner() != null) {
            //someone ate the discarded tile, so don't give any extra tile
            createNewTurn(currentPlayer, null, null);
        } else {
            // give a tile from wall to current player
            createNewTurn(currentPlayer, getTileSet().giveTilesFromWalls(1).iterator().next(), KindOfWall.WALL);
        }
        // create a new turn
    }

    private IPlayer getNewCurrentPlayer() {
        return currentTurn.getTurnWinner() != null ? currentTurn.getTurnWinner() : currentTurn.currentPlayer.getNext();
    }
}
