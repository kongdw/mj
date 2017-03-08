package org.liprudent.majiang.engine.event.impl
import org.liprudent.majiang.engine.event._
import org.liprudent.majiang.engine.round.State._
import org.liprudent.majiang.engine.tile.impl._
import org.liprudent.majiang.engine.tile._
import org.liprudent.majiang.engine.player.IPlayer.Wind._
import org.scalatest.FlatSpec

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.mock.EasyMockSugar
import org.scalatest.fixture.FixtureFlatSpec
import org.liprudent.majiang.engine.event.KindOfWall._
import org.liprudent.majiang.engine.event.KindOfHand._
import scala.collection.JavaConversions._

class JsonEventConverterSpec extends FlatSpec with ShouldMatchers {

  "A JsonEventConverter" should "convert JoinEvent" in {
    val event = new JoinEvent("jprudent", EAST, new GameInfo(new java.util.Date(), 144, 123, 13, false));
    val expected = """{"kind":"GAME_OPTIONS","name":"jprudent","gameInfo":{"wallSize":123}}"""
    expected should equal(JsonEventConverter(event));
  }

  it should "convert WindEvent" in {
    val event = new WindEvent("Zozo", NORTH);
    val expected = """{"kind":"WIND","name":"Zozo","wind":"NORTH"}"""
    expected should equal(JsonEventConverter(event));
  }

  it should "convert TilesGivenCurrentEvent" in {
    val tiles:java.util.SortedSet[ITile] = new java.util.TreeSet[ITile]();
    tiles.add(new TileSuitBamboo(0, 1))
    tiles.add(new TileSuitBamboo(0, 2))
    val event = new TilesGivenCurrentEvent("jprudent", EAST,
      WALL, CONCEALED, tiles);
    val expected = """{"kind":"CONCEALED","name":"jprudent","wind":"EAST","source":"WALL","tiles":[{"family":"BAMBOO","value":1,"id":0},{"family":"BAMBOO","value":2,"id":0}]}"""
    expected should equal(JsonEventConverter(event));
  }
  
  
}
