//package org.liprudent.majiang.engine.round.impl
//
//import org.liprudent.majiang.engine.round.impl.treatment.ITreatmentHaveANewTile
//import org.liprudent.majiang.engine.tile.impl.TileSuitBamboo
//
//import org.liprudent.majiang.engine.player.impl.Player
//import org.liprudent.majiang.engine.player.IPlayer
//
//import org.scalatest.matchers.ShouldMatchers
//import org.scalatest.FlatSpec
//import org.easymock.EasyMock
//import org.scalatest.mock.EasyMockSugar
//import org.liprudent.majiang.engine.event.KindOfWall.WALL
//import org.liprudent.majiang.engine.round.State.HAVE_A_NEW_TILE
//import org.liprudent.majiang.engine.round.State.WAIT_TILE_THROWED
//import org.liprudent.majiang.engine.round.State.END
//import org.liprudent.majiang.engine.round.impl.treatment.{ ITreatment, IEatAction, ITreatmentDeclare }
//
//import org.liprudent.majiang.testhelper.TestHelper
//class Turn2Spec extends FlatSpec with ShouldMatchers with EasyMockSugar {
//
//  "A Turn" should "give a tile to the current player when instantiated" in {
//    val player = TestHelper.makeChainedPlayers
//    val newTile = new TileSuitBamboo(0, 1)
//    val turn = new Turn2(player, newTile, WALL)
//
//    assert(player.getTiles.getConcealedHand.contains(newTile))
//  }
//
//  it should "set current player to HAVE_A_NEW_TILE and actions player to WAIT_TILE_THROWED" in {
//    val player = TestHelper.makeChainedPlayers
//    val newTile = new TileSuitBamboo(0, 1)
//    val turn = new Turn2(player, newTile, WALL)
//
//    turn.currentPlayer.getState should equal(HAVE_A_NEW_TILE)
//    turn.actionPlayers.foreach(p => p.getState should equal(WAIT_TILE_THROWED))
//  }
//
//  trait TestableTrtChowAction extends ITreatment with IEatAction {}
//  it should "in the general case, increment nbTreatment each time processTreatment is called" in {
//    val player = TestHelper.makeChainedPlayers
//    val newTile = new TileSuitBamboo(0, 1)
//    val turn = new Turn2(player, newTile, WALL)
//    val mockTrt = mock[TestableTrtChowAction]
//    expecting {
//      EasyMock.expect(mockTrt.valid).andReturn(true).once
//    }
//
//    turn.nbTreatments should equal(0)
//    turn.processTreatment(mockTrt)
//    turn.nbTreatments should equal(1)
//  }
//
//  trait TestableTrtHaveANewTile extends ITreatment with ITreatmentHaveANewTile
//  it should "not increment nbTreatment when the treatment is not valid AND it's a treatment reserved to current player" in {
//    val player = TestHelper.makeChainedPlayers
//    val newTile = new TileSuitBamboo(0, 1)
//    val turn = new Turn2(player, newTile, WALL)
//    val mockTrt = mock[TestableTrtHaveANewTile]
//    val mockPlayer = mock[IPlayer]
//    expecting {
//      EasyMock.expect(mockTrt.valid).andReturn(false).anyTimes
//    }
//
//    turn.nbTreatments should equal(0)
//    turn.processTreatment(mockTrt)
//    //in this case the treatment has no incidence
//    turn.nbTreatments should equal(0)
//  }
//
//  it should
//    "add the treatment in eatTreatmentActions when it's valid and set player's state to END" in {
//
//      val mockTrt = mock[TestableTrtChowAction]
//      val mockPlayer = mock[IPlayer]
//      expecting {
//        EasyMock.expect(mockTrt.valid).andReturn(true).anyTimes
//        EasyMock.expect(mockTrt.asInstanceOf[ITreatment].getPlayer).andReturn(mockPlayer).once
//        //should set state to END
//        EasyMock.expect(mockPlayer.setState(END)).once
//      }
//
//      val player = TestHelper.makeChainedPlayers
//      val newTile = new TileSuitBamboo(0, 1)
//      val turn = new Turn2(player, newTile, WALL)
//
//      //there is nothing before
//      turn.eatActionTreatments should have size (0)
//
//      whenExecuting(mockTrt) {
//        turn.processTreatment(mockTrt)
//      }
//
//      //there is one after
//      turn.eatActionTreatments should have size (1)
//    }
//
//  it should
//    "NOT add the treatment in eatTreatmentActions if it's invalid and should set status END to the player who initiates this invalid treatment" in {
//
//      val mockTrt = mock[TestableTrtChowAction]
//      val mockPlayer = mock[IPlayer]
//      expecting {
//        EasyMock.expect(mockTrt.valid).andReturn(false).anyTimes
//        EasyMock.expect(mockTrt.asInstanceOf[ITreatment].getPlayer).andReturn(mockPlayer).once
//        //should set status END
//        EasyMock.expect(mockPlayer.setState(END)).once
//      }
//
//      val player = TestHelper.makeChainedPlayers
//      val newTile = new TileSuitBamboo(0, 1)
//      val turn = new Turn2(player, newTile, WALL)
//      
//      //there is nothing before
//      turn.eatActionTreatments should have size (0)
//
//      whenExecuting(mockTrt) {
//        turn.processTreatment(mockTrt)
//      }
//
//      //none added after
//      turn.eatActionTreatments should have size (0)
//    }
//
//}
