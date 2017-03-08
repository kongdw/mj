package org.liprudent.majiang.engine.round

import org.liprudent.majiang.engine.tile.ITile

trait Scoring extends java.io.Serializable {
	val isMahjong:Boolean
	val fans:List[Fan]
	val total:Int
	override def toString : String = Map("isMahjong?"->isMahjong,"total"->total,"fans"->fans).toString
}

class Fan(val points:Int, val name:String, val tiles:List[ITile]) extends java.io.Serializable {
	if(name == null)
		throw new NullPointerException("name of the fan must be provided")
	if(tiles == null)
		throw new NullPointerException("tiles concerned by the fan must be provided")
	
	override def equals(o: Any):Boolean = {
		o match {
			case that:Fan => this.points == that.points && this.name == that.name && this.tiles == that.tiles && this.getClass == that.getClass
			case _ => false
		}
	}
	
	override def hashCode : Int = {
        return (41 * (41 + points) + name.hashCode + tiles.hashCode);
    }
	
	override def toString : String = {
		super.toString + Map("points"->points,"name"->name,"tiles"->tiles).toString
	}
}
