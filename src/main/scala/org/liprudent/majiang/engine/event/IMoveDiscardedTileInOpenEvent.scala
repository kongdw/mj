package org.liprudent.majiang.engine.event

import org.liprudent.majiang.engine.player.IPlayer.Wind
import java.util.SortedSet
import org.liprudent.majiang.engine.tile.ITile
/**
 * This event is raised when a player eat a discarded tile. An IMovedTilesFromConcealed must precede it.
 * @author jerome
 *
 */
trait IMoveDiscardedTileInOpenEvent extends IEvent{
	/**
	 * The discarded tile
	 */
	val discardedTile:ITile
	
	/**
	 * In which set of tiles the tile must be added 
	 */
	val into:SortedSet[ITile]
	
	/**
	 * Who discarded the tile?
	 */
	val discarderWind:Wind
}