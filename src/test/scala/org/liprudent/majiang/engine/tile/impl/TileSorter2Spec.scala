import org.liprudent.majiang.engine.tile._
import org.liprudent.majiang.engine.tile.impl._
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

class TileSorter2Spec extends FlatSpec with ShouldMatchers {
	"TileSorter2" should "find similar tiles when there are similar tiles" in {
		 val input = new StringToTile("EEEE1b").toTiles
		 val res = TileSorter2.findSimilar(input,4)
		 val expected = new StringToTile("EEEE").toTiles
		
		 res(0) should have length (4)
		 println(res(0))
		 println(expected)
		 expected.foreach{e => assert (res(0) contains (e)) } 
		 
	}
	
	it should "generate a unique key for each tile" in {
		val input = new StringToTile("E").toTiles
		println(input)
		val keys = input.map(tile=> TileSorter2.tileKey(tile))
		println(keys)
		keys(0) should equal ("EAST")
	}
}