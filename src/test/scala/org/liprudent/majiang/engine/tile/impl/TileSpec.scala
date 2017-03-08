package org.liprudent.majiang.engine.tile.impl

import org.liprudent.majiang.engine.round.State._
import org.liprudent.majiang.engine.round.Action._
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
import com.google.common.collect.Sets
import scala.collection.JavaConversions._
import org.apache.commons.collections.CollectionUtils
class TileSpec extends FlatSpec with ShouldMatchers {

  
  "A collection of tile" should "be ordered" in {
	val expected = ToTiles("EWNSRG1b2b3b4b5b6b7b8b9b1s2s3s4s5s6s7s8s9s1c2c3c4c5c6c7c8c9c")
	val shuffled = expected.reverse
	expected should equal (shuffled.sortWith((a,b)=>a.compareTo(b)<0))
  }
  
  it should "support contains method, case 1" in {
	  val sortedSet = Sets.newTreeSet(ToTiles("WNSRG1b2b3b4b5b6b7b8b9b1s2s3s4s5s6s7s8s9s1c2c3c4c5c6c7c8c9c"))
	  sortedSet.contains(new TileHonorEast(0)) should equal (false)
  }
  
  it should "support contains method, case 2" in {
	  //family=NORTH|id=2concealedHand:[family=SOUTH|id=0, family=RED|id=2, family=GREEN|id=1, family=BAMBOO|id=2|value=5, family=BAMBOO|id=1|value=8, family=BAMBOO|id=2|value=8, family=STONE|id=1|value=3, family=STONE|id=0|value=4, family=CHARACTER|id=0|value=1, family=CHARACTER|id=3|value=2, family=CHARACTER|id=2|value=3, family=CHARACTER|id=3|value=9]
	  val tiles:java.util.SortedSet[ITile] = Sets.newTreeSet(Sets.newHashSet(new TileHonorSouth(0), new TileHonorRed(2), new TileHonorGreen(1), new TileSuitBamboo(2,5), new TileSuitBamboo(1,8), new TileSuitBamboo(2,8), new TileSuitStone(1,3), new TileSuitStone(0,4), new TileSuitCharacter(0,1), new TileSuitCharacter(3,2),new TileSuitCharacter(2,3), new TileSuitCharacter(3,9)))
	  tiles.contains(new TileHonorNorth(2)) should equal (false)
  }
  
//  it should "support contains method, case 3" in {
//	  //Tile: family=CHARACTER|id=2|value=6concealedHand:[family=RED|id=0, family=BAMBOO|id=1|value=1, family=BAMBOO|id=3|value=2, family=BAMBOO|id=1|value=3, family=BAMBOO|id=0|value=6, family=BAMBOO|id=1|value=7, family=BAMBOO|id=0|value=8, family=BAMBOO|id=3|value=9, family=STONE|id=2|value=1, family=STONE|id=3|value=1, family=CHARACTER|id=2|value=5, family=CHARACTER|id=3|value=6]
//	  
//	  val tiles:java.util.SortedSet[ITile] = Sets.newTreeSet(Sets.newHashSet(new TileHonorRed(0), new TileSuitBamboo(1,1), new TileSuitBamboo(3,2), new TileSuitBamboo(1,3), new TileSuitBamboo(0,6), new TileSuitBamboo(1,7), new TileSuitBamboo(0,8), new TileSuitBamboo(3,9), new TileSuitStone(2,1), new TileSuitStone(3,1),new TileSuitCharacter(2,5), new TileSuitCharacter(3,6)))
//	  tiles.contains(new TileSuitCharacter(2,6)) should equal (false)
//  }
  
  
}
