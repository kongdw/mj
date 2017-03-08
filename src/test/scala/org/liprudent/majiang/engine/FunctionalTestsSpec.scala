package org.liprudent.majiang.engine

import org.liprudent.majiang.engine.tile.impl.ToTile
import org.liprudent.majiang.engine.tile.impl.TileHonorGreen
import org.liprudent.majiang.engine.game.impl.CrookedGame
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith
import org.liprudent.majiang.engine.tile.ITile
import org.liprudent.majiang.engine.game.IGame
import org.liprudent.majiang.engine.tile.impl.ToTiles
import org.scalatest.FeatureSpec
import org.scalatest.GivenWhenThen
import org.liprudent.majiang.testhelper.TestHelper.makeCrookedGame
import org.liprudent.majiang.engine.round.Action._
import scala.collection.JavaConversions._
import org.scalatest.matchers.ShouldMatchers
import org.liprudent.majiang.engine.round.State._
import org.liprudent.majiang.engine.round.Action._
@RunWith(classOf[JUnitRunner])
class FunctionalTestsSpec extends FeatureSpec with GivenWhenThen with ShouldMatchers {
  feature("A player can chow") {
    scenario("NORTH player has a partial suit and EAST player discard the desired tile") {
      given("NORTH player has a 1bamboo and 2bamboos in concealed hand")
      val nextTiles = ToTiles("1b 2b");

      given("EAST player has 3bamboos in concealed hand")
      val currentTiles = ToTiles("3b");

      given("a game like defined below")
      val game = makeCrookedGame(Option(currentTiles), Option(nextTiles), None, None, None)
      val north = game.findPlayer("NORTH")
      val east = game.findPlayer("EAST")
      east.getState should equal(HAVE_A_NEW_TILE)
      List("NORTH", "WEST", "SOUTH").foreach(p => game.findPlayer(p).getState should equal(WAIT_TILE_THROWED))
      east.getTiles.getConcealedHand should have size (14)
      north.getTiles.getConcealedHand should have size (13)
      nextTiles.foreach(t => north.getTiles.getConcealedHand.find(t2 => t2.getCloneValue == t.getCloneValue) should not equal (None))

      when("EAST player discards 3bamboos")
      game.handlePlayer("EAST", DISCARD_TILE, east.getTiles.getConcealedHand.find(t => t.getCloneValue == currentTiles(0).getCloneValue).get)
      east.getState should equal(END)

      when("NORT player chow")
      game.handlePlayer("NORTH", ACTION_CHOW, nextTiles.map(t => north.getTiles.getConcealedHand.find(t2 => t.getCloneValue == t2.getCloneValue).get))

      when("Others pass")
      List("WEST", "SOUTH").foreach(p => game.handlePlayer(p, PASS))

      then("NORTH player has 1,2,3bamboos in his opened hand")
      north.getTiles.getOpenedHand should have size (1)
      val chow = north.getTiles.getOpenedHand.iterator.next
      val expected = ToTiles("1b2b3b").map(t => t.getCloneValue)
      chow.filter(t => expected.contains(t.getCloneValue)) should have size (3)

      and("He has 11 tiles in his concealed hand")
      north.getTiles.getConcealedHand should have size (11)

      and("He is in state HAVE_A_NEW_TILE (he is the new current player)")
      north.getState should equal(HAVE_A_NEW_TILE)

      and("The others are in state WAIT_TILE_THROWED")
      List("EAST", "WEST", "SOUTH").foreach(p => game.findPlayer(p).getState should equal(WAIT_TILE_THROWED))
    }
  }
   feature("A player can declare a natural open kong") {
    scenario("NORTH player has a pung of Green Honors and it happens that NORTH draw the forth Green so he declare a kong") {
      given("NORTH player has two green in concealed hand")
      val nextTiles = ToTiles("GG")

      given("EAST player has also a Green in concealed hand")
      val currentTiles = ToTiles("G")
      
      given("NORTH will receive the forth G after 4 turns")
      val wall = ToTiles("EEEEG")

      given("a game like defined below")
      val game = makeCrookedGame(Option(currentTiles), Option(nextTiles), None, None, Option(wall))
      val north = game.findPlayer("NORTH")
      val east = game.findPlayer("EAST")
      val west = game.findPlayer("WEST")
      val south = game.findPlayer("SOUTH")
      east.getState should equal(HAVE_A_NEW_TILE)
      List("NORTH", "WEST", "SOUTH").foreach(p => game.findPlayer(p).getState should equal(WAIT_TILE_THROWED))
      east.getTiles.getConcealedHand should have size (14)
      north.getTiles.getConcealedHand should have size (13)
      nextTiles.foreach(t => north.getTiles.getConcealedHand.find(t2 => t2.getCloneValue == t.getCloneValue) should not equal (None))

      when("EAST player discards a Green")
      game.handlePlayer("EAST", DISCARD_TILE, east.getTiles.getConcealedHand.find(t => t.getCloneValue == currentTiles(0).getCloneValue).get)
      east.getState should equal(END)

      when("NORTH player pung")
      game.handlePlayer("NORTH", ACTION_PUNG)

      when("Others pass")
      List("WEST", "SOUTH").foreach(p => game.handlePlayer(p, PASS))

      when("this turn is finished")
      north.getTiles.getOpenedHand should have size (1)
      val pung = north.getTiles.getOpenedHand.iterator.next
      val expected = ToTiles("GGG").map(t => t.getCloneValue)
      pung.filter(t => expected.contains(t.getCloneValue)) should have size (3)
      north.getTiles.getConcealedHand should have size (11)
      north.getState should equal(HAVE_A_NEW_TILE)
      List("EAST", "WEST", "SOUTH").foreach(p => game.findPlayer(p).getState should equal(WAIT_TILE_THROWED))
      
      when("a new turn is finished (north discard a tile)")
      game.handlePlayer(north.getName, DISCARD_TILE, north.getTiles.getConcealedHand.first)
      List(east,south,west).foreach(p=>game.handlePlayer(p.getName, PASS))
      
      when("a new turn is finished (west discard a tile)")
      game.handlePlayer(west.getName, DISCARD_TILE, west.getTiles.getConcealedHand.first)
      List(east,south,north).foreach(p=>game.handlePlayer(p.getName, PASS))
      
      when("a new turn is finished (south discard a tile)")
      south.getState should equal(HAVE_A_NEW_TILE)
      List(east,north,west).foreach(p=>p.getState should equal(WAIT_TILE_THROWED))
      game.handlePlayer(south.getName, DISCARD_TILE, south.getTiles.getConcealedHand.first)
      List(east,north,west).foreach(p=>game.handlePlayer(p.getName, PASS))
      
      when("a new turn is finished (east discard a tile)")
      game.handlePlayer(east.getName, DISCARD_TILE, east.getTiles.getConcealedHand.first)
      List(north,south,west).foreach(p=>game.handlePlayer(p.getName, PASS))
      
      when("NORTH receive the third Green")
      println(north.getTiles.getConcealedHand)
      north.getTiles.getConcealedHand.find(t2 => t2.getCloneValue == ToTile("G").getCloneValue) should not equal (None)
      
      when("NORTH declare an open kong")
      game.handlePlayer(north.getName, DECLARE_OPENED_NATURAL_KONG,ToTiles("GGGG"))
      
      then("Open tiles now contains the open kong")
      north.getTiles.getOpenedHand.iterator.next.filter(t2 => t2.getCloneValue == ToTile("G").getCloneValue) should have size (4)
      
      and("NORTH is in state HAVE_A_NEW_TILE")
      north.getState should equal (HAVE_A_NEW_TILE)
      
      and("NORTH has 11 tiles in concealed hand because one had been given from dead wall")
      north.getTiles.getConcealedHand should have size (11)
    }
  }
  feature("A player can declare a hidden kong") {
    scenario("EAST player has 4 Easts in concealed hand when game starts") {
      given("EAST player has 4E in concealed hand")
      val eastTiles = ToTiles("EEEE")

      given("a game like defined below")
      val game = makeCrookedGame(Option(eastTiles), None, None, None, None)
      val east = game.findPlayer("EAST")
      eastTiles.foreach(t => east.getTiles.getConcealedHand.find(t2 => t2.getCloneValue == t.getCloneValue) should not equal (None))

      when("EAST player declare a hidden kong")
      game.handlePlayer("EAST", DECLARE_HIDDEN_NATURAL_KONG,east.getTiles.getConcealedHand.filter(t2 => t2.getCloneValue == eastTiles(0).getCloneValue))
      
      then("EAST has 11 tiles in concealed hand because one had been given from dead wall")
      east.getTiles.getConcealedHand should have size (11)
      
      and("EAST has a hidden kong")
      east.getTiles.getHiddenHand should have size (1)
      east.getTiles.getHiddenHand.iterator.next.foreach(t => eastTiles.find(t2 => t2.getCloneValue == t.getCloneValue) should not equal (None))
      
      and("EAST state is HAVE_A_NEW_TILE because a new turn started")
      east.getState should equal (HAVE_A_NEW_TILE)
    }
  }
  
  feature("A player can hu (mahjong)") {
    scenario("NORTH player is missing one tile to be able to HU and EAST player discard the desired tile") {
      given("NORTH player has a 1-9b + 1-3c + 4c in concealed hand")
      val northTiles = ToTiles("1b2b3b4b5b6b7b8b9b1c2c3c4c")

      given("EAST player has 4c in concealed hand")
      val eastTiles = ToTiles("4c")

      given("a game like defined below")
      val game = makeCrookedGame(Option(eastTiles), Option(northTiles), None, None, None)
      val north = game.findPlayer("NORTH")
      val east = game.findPlayer("EAST")
      northTiles.foreach(t => north.getTiles.getConcealedHand.find(t2 => t2.getCloneValue == t.getCloneValue) should not equal (None))

      when("EAST player discards 4c")
      game.handlePlayer("EAST", DISCARD_TILE, east.getTiles.getConcealedHand.find(t => t.getCloneValue == eastTiles(0).getCloneValue).get)
      east.getState should equal(END)

      when("NORTH player hu, so he must wait others action")
      game.handlePlayer("NORTH", ACTION_MAHJONG)
      north.getState should equal(WAIT_OTHERS_ACTION)

      when("Others pass")
      List("WEST", "SOUTH").foreach(p => game.handlePlayer(p, PASS))

      then("NORTH has state ROUND_WINNER")
      north.getState should equal(ROUND_WINNER)

      and("others has state ROUND_LOOSER")
      List("EAST", "WEST", "SOUTH").foreach(p => game.findPlayer(p).getState should equal(ROUND_LOOSER))
    }
  }
  feature("A round can be drawn (nobody win)") {
    scenario("All players pass") {
      given("a game")
      val game = makeCrookedGame(None, None, None, None, None)
      println(game.getLastRound.getTileSet)
      
      given("all players discard or pass")
      val fn = { game: CrookedGame =>
        //current discard
        val current = game.getLastRound.getCurrentPlayer
        val tile = current.getTiles.getConcealedHand.iterator.next
        game.handlePlayer(current.getName, DISCARD_TILE, tile)
        //others pass
        List(current.getNext, current.getNext.getNext, current.getNext.getNext.getNext).foreach(p =>
          game.handlePlayer(p.getName, PASS))
      }
      while (!game.getLastRound.getTileSet.getWall.isEmpty) {
        fn(game)
      } 
      fn(game)
      
      then("no tiles left in the wall")
      println("!!!!!!!!!!!!!!!!!!!!!!!wall size " + game.getLastRound.getTileSet.getWall.size)
      game.getLastRound.getTileSet.getWall.isEmpty should be(true)

      then("Every player has state ROUND_LOOSER")
      List("EAST", "NORTH", "WEST", "SOUTH").foreach(w =>
        game.findPlayer(w).getState should equal(ROUND_LOOSER))
    }
  }
}