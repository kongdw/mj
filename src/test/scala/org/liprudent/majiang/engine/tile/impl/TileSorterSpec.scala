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
class TileSorterSpec extends FlatSpec with ShouldMatchers {

  object TreeSet {
    def apply(it: Iterable[ITile]): java.util.TreeSet[ITile] = {
      val ret = new java.util.TreeSet[ITile]()
      ret.addAll(it);
      ret
    }
  }
  "TileSorter" should "find similar tiles" in {
    val hand = TreeSet(List(new TileHonorEast(0), new TileHonorEast(1), new TileHonorEast(2), new TileHonorEast(3),
      new TileSuitBamboo(0, 1), new TileSuitBamboo(1, 1), new TileSuitBamboo(2, 1), new TileSuitBamboo(3, 1)))
    val expected = List(TreeSet(List(new TileHonorEast(0), new TileHonorEast(1), new TileHonorEast(2))),
      TreeSet(List(new TileHonorEast(0), new TileHonorEast(1), new TileHonorEast(3))),
      TreeSet(List(new TileHonorEast(0), new TileHonorEast(2), new TileHonorEast(3))),
      TreeSet(List(new TileHonorEast(1), new TileHonorEast(2), new TileHonorEast(3))),
      TreeSet(List(new TileSuitBamboo(0, 1), new TileSuitBamboo(1, 1), new TileSuitBamboo(2, 1))),
      TreeSet(List(new TileSuitBamboo(0, 1), new TileSuitBamboo(1, 1), new TileSuitBamboo(3, 1))),
      TreeSet(List(new TileSuitBamboo(0, 1), new TileSuitBamboo(2, 1), new TileSuitBamboo(3, 1))),
      TreeSet(List(new TileSuitBamboo(1, 1), new TileSuitBamboo(2, 1), new TileSuitBamboo(3, 1))))
    val result = TileSorter.findSimilar(hand, 3)
    result should have size (8)
    result.foreach(res => expected should contain(res))

  }

  it should "not find a PUNG when there is no PUNG" in {
    val hand = TreeSet(List(
      new TileHonorEast(2),
      new TileHonorEast(3),
      new TileHonorWest(3),
      new TileHonorEast(3),
      new TileSuitBamboo(2, 1),
      new TileSuitStone(0, 3),
      new TileSuitStone(2, 6),
      new TileSuitCharacter(0, 4),
      new TileSuitCharacter(3, 9),
      new TileSuitBamboo(2, 6)))
    
      TileSorter.findPung(hand) should have size(0) 
  }

}
