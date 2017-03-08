package org.liprudent.majiang.engine.tile.impl

import org.liprudent.majiang.engine.tile._

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

class SpecialPungTest extends FlatSpec with ShouldMatchers {
	"SpecialPung" should "provide 136 tiles" in {
		val lst = new SpecialPung().toTiles
		assert(lst.length == 136)
	}
}

class SpecialChowTest extends FlatSpec with ShouldMatchers {
	"SpecialChow" should "provide 136 tiles" in {
		val lst = new SpecialChow().toTiles
		assert(lst.length == 136)
	}
}
class SpecialKongTest extends FlatSpec with ShouldMatchers {
	"SpecialKong" should "provide 136 tiles" in {
		val lst = new SpecialKong().toTiles
		assert(lst.length == 136)
	}
}

class StringToTilesSpec extends FlatSpec with ShouldMatchers {
	"StringToTile" must "understand that 'E' stands for ITileHonorEast" in {
		val lst = new StringToTile("E").toTiles
		assert(lst.length == 1)
		assert(lst(0) equals new TileHonorEast(0))
	}
	
	it must "understand that 'W' stands for ITileHonorWest" in {
		val lst = new StringToTile("W").toTiles
		assert(lst.length == 1)
		assert(lst(0) equals new TileHonorWest(0))
	}
	
	it must "understand that 'S' stands for ITileHonorSouth" in {
		val lst = new StringToTile("S").toTiles
		assert(lst.length == 1)
		assert(lst(0) equals new TileHonorSouth(0))
	}
	
	it must "understand that 'N' stands for ITileHonorNorth" in {
		val lst = new StringToTile("N").toTiles
		assert(lst.length == 1)
		assert(lst(0) equals new TileHonorNorth(0))
	}
	
	it must "understand that 'B' stands for ITileHonorWhite" in {
		val lst = new StringToTile("B").toTiles
		assert(lst.length == 1)
		assert(lst(0) equals new TileHonorWhite(0))
	}
	
	it must "understand that 'G' stands for ITileHonorGreen" in {
		val lst = new StringToTile("G").toTiles
		assert(lst.length == 1)
		assert(lst(0) equals new TileHonorGreen(0))
	}
	
	it must "understand that 'R' stands for ITileHonorRed" in {
		val lst = new StringToTile("R").toTiles
		assert(lst.length == 1)
		assert(lst(0) equals new TileHonorRed(0))
	}
	
	it must "understand that ENSWRBG stands for east,north,south,west,red,green,white" in {
		val expected = List(
			new TileHonorEast(0),
			new TileHonorNorth(0),
			new TileHonorSouth(0),
			new TileHonorWest(0),
			new TileHonorRed(0),
			new TileHonorGreen(0),
			new TileHonorWhite(0))
		
		val lst = new StringToTile("ENSWRGB").toTiles
		assert(lst equals expected)
	}
	
	it must "understand that BGRWSNE stands for white,green,red,west,south,north,east" in {
		val expected = List(
			new TileHonorEast(0),
			new TileHonorNorth(0),
			new TileHonorSouth(0),
			new TileHonorWest(0),
			new TileHonorRed(0),
			new TileHonorGreen(0),
			new TileHonorWhite(0)).reverse
		
		val lst = new StringToTile("BGRWSNE").toTiles
		assert(lst equals expected)
	}
	
	it must "understand that WWW stands for List(new TileHonorWest(0),new TileHonorWest(1),new TileHonorWest(2))" in {
		val expected = List(new TileHonorWest(0),new TileHonorWest(1),new TileHonorWest(2))
		val lst = new StringToTile("WWW").toTiles
		assert(lst equals expected)
	}
	
	it must "understand that 3WB2R stands for List(new TileHonorWest(0),new TileHonorWest(1),new TileHonorWest(2), new TileHonorWhite(0),new TileHonorRed(0),new TileHonorRed(1))" in {
		val expected = List(new TileHonorWest(0),new TileHonorWest(1),new TileHonorWest(2), new TileHonorWhite(0),new TileHonorRed(0),new TileHonorRed(1))
		val lst = new StringToTile("3WB2R").toTiles
		assert(lst equals expected)
	}
	
	it must "understand that 1b1c1s stands for List(new TileSuitBamboo(0,1),new TileSuitCharacter(0,1),new TileSuitStone(0,1))" in {
		val expected = List(new TileSuitBamboo(0,1),new TileSuitCharacter(0,1),new TileSuitStone(0,1))
		val lst = new StringToTile("1b1c1s").toTiles
		assert(lst equals expected)
	}
	
	it must "understand that 41b stands for List(new TileSuitBamboo(0,1),new TileSuitBamboo(1,1),new TileSuitBamboo(2,1),new TileSuitBamboo(3,1))" in {
		val expected = List(new TileSuitBamboo(0,1),new TileSuitBamboo(1,1),new TileSuitBamboo(2,1),new TileSuitBamboo(3,1)) 
		val lst = new StringToTile("41b").toTiles
		assert(lst equals expected)
	}
	it must "understand that 1b2b3b4b5b6b7b8b9b stands for List(new TileSuitBamboo(0,1),new TileSuitBamboo(0,2),new TileSuitBamboo(0,3),new TileSuitBamboo(0,4),new TileSuitBamboo(0,5),new TileSuitBamboo(0,6),new TileSuitBamboo(0,7),new TileSuitBamboo(0,8),new TileSuitBamboo(0,9))" in {
		val expected = List(new TileSuitBamboo(0,1),new TileSuitBamboo(0,2),new TileSuitBamboo(0,3),new TileSuitBamboo(0,4),new TileSuitBamboo(0,5),new TileSuitBamboo(0,6),new TileSuitBamboo(0,7),new TileSuitBamboo(0,8),new TileSuitBamboo(0,9))
		val lst = new StringToTile("1b2b3b4b5b6b7b8b9b").toTiles
		assert(lst equals expected)
	}
	it must "understand that 41bW2N11s stands for List(new TileSuitBamboo(0,1),new TileSuitBamboo(1,1),new TileSuitBamboo(2,1),new TileSuitBamboo(3,1), new TileHonorWest(0), new TileHonorNorth(0), new TileHonorNorth(1), new TileSuitStone(0,1))" in {
		val expected = List(new TileSuitBamboo(0,1),new TileSuitBamboo(1,1),new TileSuitBamboo(2,1),new TileSuitBamboo(3,1), new TileHonorWest(0), new TileHonorNorth(0), new TileHonorNorth(1), new TileSuitStone(0,1))
		val lst = new StringToTile("41bW2N11s").toTiles
		assert(lst equals expected)
	}
	it must "understand that 41b W 2N 11s stands for List(new TileSuitBamboo(0,1),new TileSuitBamboo(1,1),new TileSuitBamboo(2,1),new TileSuitBamboo(3,1), new TileHonorWest(0), new TileHonorNorth(0), new TileHonorNorth(1), new TileSuitStone(0,1))" in {
		val expected = List(new TileSuitBamboo(0,1),new TileSuitBamboo(1,1),new TileSuitBamboo(2,1),new TileSuitBamboo(3,1), new TileHonorWest(0), new TileHonorNorth(0), new TileHonorNorth(1), new TileSuitStone(0,1))
		val lst = new StringToTile("41b W2 N 11s").toTiles
		assert(lst equals expected)
	}
}