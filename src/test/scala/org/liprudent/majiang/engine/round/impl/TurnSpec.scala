package org.liprudent.majiang.engine.round.impl

import org.liprudent.majiang.engine.round.State._
import org.liprudent.majiang.engine.round.Action._
import org.scalatest.FlatSpec
import org.liprudent.majiang.engine.tile.impl._
import org.liprudent.majiang.engine.tile._
import org.scalatest.matchers.ShouldMatchers
import com.google.common.collect._
import org.liprudent.majiang.engine.player.IPlayer
import org.liprudent.majiang.engine.player.IPlayer.Wind._
import org.liprudent.majiang.engine.player.impl.Player
import org.liprudent.majiang.engine.game.impl._
import org.liprudent.majiang.engine.round.impl.treatment.{ ITreatment, IEatAction, ITreatmentDeclare }
import org.easymock.EasyMock
import org.scalatest.mock.EasyMockSugar
import org.liprudent.majiang.testhelper.TestHelper
import org.liprudent.majiang.engine.event.KindOfWall.WALL

class TurnSpec extends FlatSpec with ShouldMatchers with EasyMockSugar {

  trait TestableTrtChowAction extends ITreatment with IEatAction {}

  "A Turn" should "give a tile to the current player when instantiated" in {
    val player = TestHelper.makeChainedPlayers
    val newTile = new TileSuitBamboo(0, 1)
    val turn = new Turn(player, newTile, WALL)

    assert(player.getTiles.getConcealedHand.contains(newTile))
  }

  it should "set current player to HAVE_A_NEW_TILE and actions player to WAIT_TILE_THROWED" in {
    val player = TestHelper.makeChainedPlayers
    val newTile = new TileSuitBamboo(0, 1)
    val turn = new Turn(player, newTile, WALL)

    turn.currentPlayer.getState should equal(HAVE_A_NEW_TILE)
    val it = turn.actionPlayers.iterator
    while (it.hasNext) it.next.getState should equal(WAIT_TILE_THROWED)
  }

  it should
    "NOT add the treatment in his list if it's invalid and should set status END to the player who initiates this invalid treatment" in {

      val mockTrt = mock[TestableTrtChowAction]
      val mockPlayer = mock[IPlayer]
      expecting {
        EasyMock.expect(mockTrt.valid).andReturn(false).once
        EasyMock.expect(mockTrt.asInstanceOf[ITreatment].getPlayer).andReturn(mockPlayer).once
        EasyMock.expect(mockPlayer.setState(END)).once
      }

      val player = TestHelper.makeChainedPlayers
      val newTile = new TileSuitBamboo(0, 1)
      val turn = new Turn(player, newTile, WALL)
      val nbTrtBefore = turn.getNbTreatments
      whenExecuting(mockTrt) {
        //treatment is invalid but the engine registered it
    	  turn.processTreatment(mockTrt) should equal(false)
        turn.getNbTreatments should equal(nbTrtBefore + 1)
      }

      turn.getEatActions should have size (0)
    }

  trait TestableTrtDeclareHiddenKong extends ITreatment with ITreatmentDeclare

  it should "end if current player declare a hidden kong, the current player is the turn winner" in {
    val mockTrt = mock[TestableTrtDeclareHiddenKong]
    val mockPlayer = mock[IPlayer]
    expecting {
      EasyMock.expect(mockTrt.valid).andReturn(true).once
      EasyMock.expect(mockTrt.doTreatment).once
      EasyMock.expect(mockTrt.asInstanceOf[ITreatment].getPlayer).andReturn(mockPlayer).once
      EasyMock.expect(mockTrt.changeStates).once
    }
    val player = TestHelper.makeChainedPlayers
    val newTile = new TileSuitBamboo(0, 1)
    val turn = new Turn(player, newTile, WALL)
    whenExecuting(mockTrt) {
      turn.processTreatment(mockTrt) should equal(true)
    }

    assert(turn.endTurn equals true)
    assert(turn.isKongTurnWinner equals true)
    assert(turn.getTurnWinner equals mockPlayer)
  }
}
