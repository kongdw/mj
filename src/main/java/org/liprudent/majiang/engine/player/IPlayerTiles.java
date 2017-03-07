package org.liprudent.majiang.engine.player;

import java.io.Serializable;
import java.util.Set;
import java.util.SortedSet;

import org.liprudent.majiang.engine.tile.ITile;

public interface IPlayerTiles extends Serializable {
	/**
	 * @return Tiles player has in front of him. He's the only one who can see
	 *         them. There is a maximum of 13 or 14 tiles in this set.
	 */
	SortedSet<ITile> getConcealedHand();

	/**
	 * @return Tiles which are hidden and cannot be changed anymore(used for
	 *         natural Kong ). This Set is empty when round start. The set
	 *         contains only 4 of a kind.
	 */
	Set<SortedSet<ITile>> getHiddenHand();

	/**
	 * @return Tiles which are opened to every player and cannot be changed
	 *         anymore(used for pung or chow or Kong). This Set is empty when
	 *         round start. The set contains set of suits, or 3/4 of a kind.
	 */
	Set<SortedSet<ITile>> getOpenedHand();
	
	/**
	 * @return The winning tile if any, null otherwise
	 */
	WinningTile getWinningTile();
	
	void setWinningTile(ITile tile);
}
