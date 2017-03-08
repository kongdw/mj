package org.liprudent.majiang.engine.player.impl




import org.liprudent.majiang.engine.tile._
import org.liprudent.majiang.engine.tile.impl._
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.mock.EasyMockSugar
import scala.collection.JavaConversions._
import com.google.common.collect.Sets

class TileSetSpec extends FlatSpec with ShouldMatchers with EasyMockSugar {


  "A PlayerTiles" should "set the winning tile when a tile is moved in concealed hand" in {
	  val pTiles = new PlayerTiles
	  val tile = new TileHonorEast(0)
	  pTiles.giveTileInConcealedHand(tile)
	  pTiles.getWinningTile.tile should equal(tile)
  }
  
  it should "set the winning tile when several tiles is moved in concealed hand" in {
	  val pTiles = new PlayerTiles
	  val tiles:java.util.List[ITile] = ToTiles("NSWE");
	   val tile = new TileHonorEast(0)
	  pTiles.giveTilesInConcealedHand(tiles)
	  pTiles.getWinningTile.tile should equal(tile)
  }
  
   it should "remove tile from concealed hand when discard is called" in {
	  val pTiles = new PlayerTiles
	  val tiles:java.util.SortedSet[ITile] = Sets.newTreeSet(Sets.newHashSet(new TileHonorSouth(0), new TileHonorRed(2), new TileHonorGreen(1), new TileSuitBamboo(2,5), new TileSuitBamboo(1,8), new TileSuitBamboo(2,8), new TileSuitStone(1,3), new TileSuitStone(0,4), new TileSuitCharacter(0,1), new TileSuitCharacter(3,2),new TileSuitCharacter(2,3), new TileSuitCharacter(3,9)))
	  pTiles.giveTilesInConcealedHand(tiles)
	  pTiles.getConcealedHand().size should equal (12)
	   
	  pTiles.discardTile(new TileHonorSouth(0)) 
	  pTiles.getConcealedHand().size should equal (11)
	  pTiles.getConcealedHand() should not contain(new TileHonorSouth(0))
  }
}
