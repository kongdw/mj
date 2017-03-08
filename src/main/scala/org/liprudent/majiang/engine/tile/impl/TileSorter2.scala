package org.liprudent.majiang.engine.tile.impl
import org.liprudent.majiang.engine.tile.{ITile,ITileSuit}

/**
 * @author jerome
 *@Deprecated
 */
object TileSorter2 {
  def findSimilar(input: List[ITile],  nbSimilar: Int): List[List[ITile]] = {

    var sorted: Map[String, List[ITile]] = Map()
    input.foreach { tile ⇒
      val key = tileKey(tile)
      val value = sorted.getOrElse(key, Nil)
      sorted = sorted + (key -> (tile :: value))
    }

    var ret: List[List[ITile]] = Nil
    sorted.values.foreach { v ⇒
      if (v.length >= nbSimilar)
        ret = v.take(nbSimilar) :: ret
    }
    
    return ret
    
  }
  
  def findKong(input: List[ITile]): List[List[ITile]] = {findSimilar(input,4)}
  
  def tileKey(tile:ITile):String = { 
	  val family = tile.getFamily.toString
	  val value = tile match {
        case t: ITileSuit ⇒ t.asInstanceOf[ITileSuit].getValue.toString
        case _ ⇒ ""
      }
	  return family + value
  }
  
}
