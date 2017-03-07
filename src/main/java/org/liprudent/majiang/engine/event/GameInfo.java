package org.liprudent.majiang.engine.event;

import java.io.Serializable;
import java.util.Date;

public class GameInfo implements Serializable {
	private final Date creation;
	private final Integer nbTiles;
	private final Integer wallSize;
	private final Integer deadWallSize;
	private final Boolean withFlowers;
	
	
	public GameInfo(Date creation, Integer nbTiles, Integer wallSize,
			Integer deadWallSize, Boolean withFlowers) {
		super();
		this.creation = creation;
		this.nbTiles = nbTiles;
		this.wallSize = wallSize;
		this.deadWallSize = deadWallSize;
		this.withFlowers = withFlowers;
	}

	public Boolean getWithFlowers() {
		return withFlowers;
	}
	
	public Date getCreation() {
		return creation;
	}
	public Integer getNbTiles() {
		return nbTiles;
	}
	public Integer getWallSize() {
		return wallSize;
	}
	public Integer getDeadWallSize() {
		return deadWallSize;
	}
	
	
}
