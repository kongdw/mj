package org.liprudent.majiang.engine.round.impl

import org.liprudent.majiang.engine.round.State._
import org.liprudent.majiang.engine.round.IRound
import org.liprudent.majiang.engine.round.Action._
import org.liprudent.majiang.engine.player.IPlayer

import org.scalatest.FlatSpec
import org.liprudent.majiang.engine.tile.impl._
import org.liprudent.majiang.engine.tile._
import org.scalatest.matchers.ShouldMatchers
import com.google.common.collect._
import org.liprudent.majiang.engine.player.IPlayer.Wind._
import org.liprudent.majiang.engine.player.impl.Player
import org.liprudent.majiang.engine.game.impl._
import org.slf4j.LoggerFactory
import org.liprudent.majiang.testhelper.TestHelper
import org.scalatest.mock.EasyMockSugar
import org.easymock.EasyMock
import org.liprudent.majiang.engine.event.KindOfWall

class RoundSpec extends FlatSpec with ShouldMatchers with EasyMockSugar {

  private val log = LoggerFactory.getLogger("Round")
  val players: java.util.Set[org.liprudent.majiang.engine.player.IPlayer] = Sets.newHashSet(Player.instanceOf("PLAYER_1", EAST), Player.instanceOf("PLAYER_2", NORTH), Player.instanceOf("PLAYER_3", WEST), Player.instanceOf("PLAYER_4", SOUTH))

  "A round" should "give a tile from the dead wall when a player kong" in {
    val crookedWall = new SpecialKong2().toTiles
    val game = new CrookedGame(players, new SpecialKong2().toTiles)
    val p1 = game.findPlayer("PLAYER_1")
    val p2 = game.findPlayer("PLAYER_2")
    val p3 = game.findPlayer("PLAYER_3")
    val p4 = game.findPlayer("PLAYER_4")
    p1.getNext should equal(p2)

    //PLAYER 1 has one east
    val p1East: ITile = new TileHonorEast(0)
    p1.getTiles.getConcealedHand should contain(p1East)

    //player 1 discard his east
    game.handlePlayer(p1.getName, DISCARD_TILE, p1East)

    //player 2 has 3 EAST
    val p2MustHaveEast: List[ITile] = List(new TileHonorEast(1), new TileHonorEast(2), new TileHonorEast(3))
    p2MustHaveEast.foreach(tile => p2.getTiles.getConcealedHand should contain(tile))

    //player2 kong
    val sizeDeadWallBeforeKong = game.getLastRound.getTileSet.getDeadWall.size
    game.handlePlayer(p2.getName, ACTION_KONG)

    //player 3 an 4 pass
    game.handlePlayer(p3.getName, PASS)
    game.handlePlayer(p4.getName, PASS)

    //so, the kong is now in p2 opened hand
    p2.getTiles.getOpenedHand() should have size (1)
    val theKong = p2.getTiles.getOpenedHand().iterator.next
    (p1East :: p2MustHaveEast).foreach(tile => theKong should contain(tile))

    //so, p2 concealed hand must have (13 + 1) - 4 + 1 tiles because one new tile from the dead wall
    p2.getTiles.getConcealedHand should have size (11)
    game.getLastRound.getTileSet.getDeadWall should have size (sizeDeadWallBeforeKong - 1)

    p2.getState should equal(HAVE_A_NEW_TILE)

  }

  it should " end when to tiles left in the wall and the turn is finished (all players made his action)" in {
    val emptyTileSets = new TileSets();

    val mockTurn = mock[Turn]
    expecting {
      EasyMock.expect(mockTurn.endRoundBecauseMahjong).andReturn(false).once
      EasyMock.expect(mockTurn.endTurn).andReturn(true).once
    }
    val round = new Round(TestHelper.makeChainedPlayers) {
      override protected def createNewTurn(currentPlayer: IPlayer, extraTile: ITile, source: KindOfWall) = {
        currentTurn = mockTurn
        turns = currentTurn :: turns
      }
      override lazy val tileSet = emptyTileSets
    }

    emptyTileSets.giveTilesFromWalls(emptyTileSets.getWall.size)
    emptyTileSets.getWall.isEmpty should equal(true)
    whenExecuting(mockTurn) {
      round.isEnd should equal(true)
    }

  }

}
