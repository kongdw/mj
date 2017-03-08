package org.liprudent.majiang.engine.event

import org.liprudent.majiang.engine.tile.ITile
import java.util.SortedSet
trait IMoveTileInOpenKongEvent extends IEvent{
	/**
	 * The tile
	 */
	val ownedTile:ITile
	
	/**
	 * In which pung tile must be added 
	 */
	val pung:SortedSet[ITile]
	
}