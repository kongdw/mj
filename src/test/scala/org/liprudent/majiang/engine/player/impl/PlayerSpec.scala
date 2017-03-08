package org.liprudent.majiang.engine.player.impl

import org.liprudent.majiang.engine.tile._
import org.liprudent.majiang.engine.player.IPlayer.Wind.EAST
import org.liprudent.majiang.engine.event.KindOfWall._
import org.liprudent.majiang.engine.tile.impl._
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.mock.EasyMockSugar
import scala.collection.JavaConversions._
import com.google.common.collect.Sets
import java.util.TreeSet
import java.util.SortedSet

class PlayerSpec extends FlatSpec with ShouldMatchers with EasyMockSugar {

  "A Player" should "be able to take a discarded tile and add it open hand" in {
    val p = Player.instanceOf("fakir")
    
    val tiles:SortedSet[ITile] = new TreeSet(Set(new TileHonorEast(0), new TileHonorEast(1)));
    p.giveTilesInConcealedHand(tiles, WALL)
    p.moveFromConcealedToOpen(tiles)
    val expected = new TreeSet(tiles)
    expected should equal(p.getTiles.getOpenedHand.iterator.next)

    val discarded = new TileHonorEast(2)
    p.moveDiscardedTileInOpen(discarded, tiles, EAST)

    expected.add(discarded)
    expected should equal(p.getTiles.getOpenedHand.iterator.next)
  }

}
