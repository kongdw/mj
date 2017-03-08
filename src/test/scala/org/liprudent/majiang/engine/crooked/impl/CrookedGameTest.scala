package org.liprudent.majiang.engine.game.impl

import org.scalatest.FlatSpec
import org.liprudent.majiang.engine.tile.impl._
import org.liprudent.majiang.engine.tile._
import org.scalatest.matchers.ShouldMatchers
import com.google.common.collect._
import org.liprudent.majiang.engine.player.IPlayer.Wind._
import org.liprudent.majiang.engine.player.impl.Player
import scala.collection.JavaConversions._

class CrookedGameSpec extends FlatSpec with ShouldMatchers {
	
  val players :java.util.Set[org.liprudent.majiang.engine.player.IPlayer]  = Sets.newHashSet(Player.instanceOf("PLAYER_1",EAST), Player.instanceOf("PLAYER_2",NORTH), Player.instanceOf("PLAYER_3",WEST),Player.instanceOf("PLAYER_4",SOUTH))
  val crookedWall = new StringToTile("ESWNGRB1b2b3b4b5b6b 8b9b1c2c3c4c5c6c7c8c9c1s2s 3s4s5s6s7s8s9sESWNGR B1b2b3b4b5b6b7b8b9b1c2c3c 7b4c5c6c7c8c9c1s2s3s4s5s6s7s8s9sESWNGRB1b2b3b4b5b6b7b8b9b1c2c3c4c5c6c7c8c9c1s2s3s4s5s6s7s8s9sESWNGRB1b2b3b4b5b6b7b8b9b1c2c3c4c5c6c7c8c9c1s2s3s4s5s6s7s8s9s").toTiles
  println(crookedWall.length)
  assert(crookedWall.length == 136)
  
  "A crooked game" should "provide a prepared wall so that we can predict how tiles will be drawn" in {
  	 val crooked = new CrookedGame(players,crookedWall)
  	 val expected = ToTiles("ESWNGRB1b2b3b4b5b6b7b")
  	 crooked.findPlayer("PLAYER_1").getTiles.getConcealedHand().foreach(t=> expected.find(t2=>t.getCloneValue == t2.getCloneValue) should not equal (None))
  }
  
  "A crooked TileSet" should "compose a wall the way we told" in {
  	val crooked = new CrookedTileSets(crookedWall)
  	println(crooked.getWall.size)
  	println(TileSets.WALL_SIZE)
  	assert(crooked.getWall.size == TileSets.WALL_SIZE)
  	(0 until TileSets.WALL_SIZE).foreach(i=>assert(crooked.getWall.get(i) equals crookedWall(i)))
  }
  
}
