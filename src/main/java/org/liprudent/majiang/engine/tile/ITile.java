package org.liprudent.majiang.engine.tile;

import java.io.Serializable;

/**
 * 抽象的麻将牌
 */
public interface ITile extends Comparable<ITile>, Serializable{
	/**
	 * @return The kind of family the tile belongs to.
	 */
	TileFamily getFamily();

	/**
	 * There are 4 copies of each tile
	 * 
	 * @return an integer between 0 and 3
	 */
	int getId();

	/**
	 * 
	 * @param obj
	 *            The tile we want to compare to
	 * @return True if all attributes of obj, except id, are equals to this.
	 */
	boolean compare(ITile obj);

	/**
	 * @return a value that is different for all tiles but equals for cloned tiles
	 */
	int getCloneValue();
	
	/**
	 * @return A unique id among all the tiles in a game
	 */
	String getUniqueId();
}
