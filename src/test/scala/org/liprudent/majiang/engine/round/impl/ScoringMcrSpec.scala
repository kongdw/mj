package org.liprudent.majiang.engine.round.impl 

import org.liprudent.majiang.engine.tile.impl.TileSuitBamboo
import org.liprudent.majiang.engine.tile.impl.ToTile
import javax.script.ScriptEngine
import javax.script.ScriptEngineManager
import javax.script.ScriptException
import org.scalatest.FlatSpec
import org.scalatest.fixture.FixtureFlatSpec

import org.scalatest.matchers.ShouldMatchers

import org.liprudent.majiang.engine.tile.impl.StringToTile
import org.liprudent.majiang.engine.tile.impl.ToTiles
import org.liprudent.majiang.engine.tile.ITile

import org.liprudent.majiang.engine.round.Fan
import org.liprudent.majiang.engine.player.IPlayerTiles
import org.liprudent.majiang.engine.player.WinningTile

import org.easymock.EasyMock
import org.scalatest.mock.EasyMockSugar

import com.google.common.collect.Sets
import scala.collection.JavaConversions._

class ScoringMcrSpec extends FixtureFlatSpec with ShouldMatchers with EasyMockSugar {

	type FixtureParam = (List[ITile],IPlayerTiles)
	
	def withFixture(test: OneArgTest) {
		val visibleTiles = List(new TileSuitBamboo(0,7),new TileSuitBamboo(1,7),new TileSuitBamboo(2,7))
		val playerTiles = mock[IPlayerTiles]
        expecting{
			val w = new TileSuitBamboo(3,7)
			val win = new WinningTile(w,false,false,false,false)
			//tile 7b (winning tile) is repeated in concealed, that's how it will be in the game
			val c:java.util.SortedSet[ITile] = Sets.newTreeSet(w :: ToTiles("1b1b5b6b"))
			val h:java.util.SortedSet[ITile] = Sets.newTreeSet(ToTiles("49b"))
			val o1:java.util.SortedSet[ITile] = Sets.newTreeSet(ToTiles("1b2b3b"))
			val o2:java.util.SortedSet[ITile] = Sets.newTreeSet(ToTiles("4b5b6b"))
            EasyMock.expect(playerTiles.getConcealedHand).andReturn(c).once
            EasyMock.expect(playerTiles.getHiddenHand).andReturn(Set(h)).once
            EasyMock.expect(playerTiles.getOpenedHand).andReturn(Set(o1,o2)).anyTimes
            EasyMock.expect(playerTiles.getWinningTile).andReturn(win).once
        }
		test(visibleTiles,playerTiles)
	}
	
	"PythonEngine" should "import majiang.py" in { () =>
		PythonEngine.engine.eval("m = find_mahjong('m b1b2b3 m b4b5b6 c b9b9b9b9 h b1b1b5b6 w b7 v b7b7b7')")
		val m = PythonEngine.engine.get("m")
		print(m)
		assert(m!=null)
	}
	
	"ScoringMcr" should "return the score" in { fixture =>
		
		val (visibleTiles,playerTiles) = fixture
		
		whenExecuting(playerTiles){
			val total = new ScoringMcr(visibleTiles,playerTiles).total 
			total should equal (32)
		}
	}
	
	it should "return wether it's a mahjong or not" in { fixture =>
		
		val (visibleTiles,playerTiles) = fixture
		
		whenExecuting(playerTiles){
			new ScoringMcr(visibleTiles,playerTiles).isMahjong should be (true) 
		}
		
	}
	
	it should "return the fans" in {fixture =>
		println("ta reum la teupu")
		val (visibleTiles,playerTiles) = fixture
		
		val expectedNbFans = 5
		val f1 = new Fan(24,"Full Flush",ToTiles("1b2b3b 4b5b6b 49b 1b1b 5b6b7b"))
		val f2 = new Fan(4,"Last Tile",ToTiles("7b"))
		val f3 = new Fan(2, "Concealed Kong", ToTiles("9b9b9b9b"))
		val f4 = new Fan(1, "Short Straight", ToTiles("1b2b3b4b5b6b"))
		val f5 = new Fan(1, "Pung of Terminals or Honors", ToTiles("9b9b9b9b"))			
		val expectedFans = List(f1,f2,f3,f4,f5)
		
		assert(expectedFans(0).tiles.size == 15)
		whenExecuting(playerTiles){
			val fans = new ScoringMcr(visibleTiles,playerTiles).fans 
			fans.size should equal (expectedNbFans)
			(0 until expectedNbFans).foreach( i=>{
				fans(i).name should equal (expectedFans(i).name)
				assert(fans(i).points == expectedFans(i).points)
				(0 until (expectedFans(i).tiles.size)).foreach( j =>
					fans(i).tiles(j).compare(expectedFans(i).tiles(j)) should equal (true)
				)
			})
		}
	}

	"PyMCRNotation" should "convert to Py-MCR notation" in {fixture =>
		
		val (visibleTiles,playerTiles) = fixture

        whenExecuting(playerTiles) {
                val s = PyMcrNotation(visibleTiles,playerTiles)
                s should equal("m b1b2b3 m b4b5b6 c b9b9b9b9 h b1b1b5b6 w b7 v b7b7b7")
        }
		
	} 
	
	it should "not add tiles in opened hand in visible tiles" in {fixture =>
		
		val (visibleTiles,playerTiles) = fixture
		//add some tiles of player's opened hand in visible tiles
		val visibleTilesWithSomeOpened = visibleTiles ::: ToTiles("1b2b3b")
		println(visibleTilesWithSomeOpened)
		whenExecuting(playerTiles) {
                val s = PyMcrNotation(visibleTilesWithSomeOpened,playerTiles)
                s should equal("m b1b2b3 m b4b5b6 c b9b9b9b9 h b1b1b5b6 w b7 v b7b7b7")
        }
		
	}
	
	it should "not add the winnning tile in visible tiles" in {fixture =>
		
		val (visibleTiles,playerTiles) = fixture
		//The use case is for instance: there is a pung of 7b, and a 7b just had been discarded, so 4 7b are visible
		val visibleTilesWithSomeOpened = new TileSuitBamboo(3,7) :: visibleTiles
		whenExecuting(playerTiles) {
                val s = PyMcrNotation(visibleTilesWithSomeOpened,playerTiles)
                s should equal("m b1b2b3 m b4b5b6 c b9b9b9b9 h b1b1b5b6 w b7 v b7b7b7")
        }
		
	}
}
