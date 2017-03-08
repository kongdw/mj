
package org.liprudent.majiang.engine.player {
	
	import org.liprudent.majiang.engine.tile.ITile
	
	@serializable class WinningTile(val tile:ITile, val selfDrawn:Boolean, val lastTurn:Boolean, val kongReplacement:Boolean, val kongRobbing:Boolean){
	
		checkNotNull(tile,"The tile must be provided")
		checkNotNull(selfDrawn,"Wether the tile had been self drawn must be provided")
		checkNotNull(lastTurn,"Wether the tile had been drawn on last turn must be provided")
		checkNotNull(kongReplacement,"Wether the tile had been drawn when replacing a tile that forms a kong must be provided")
		checkNotNull(kongRobbing,"Wether the tile had been robbed when opponent is forming a kong must be provided")
		
		private def checkNotNull(o:Any,message:String){
			if(o == null) throw new java.lang.NullPointerException(message)
		}
		
		override def toString = "[tile:" + tile + ",selfDrawn:" + selfDrawn + ",lastTurn:"+lastTurn+",kongReplacement:"+kongReplacement+",kongRobbing"+kongRobbing+"]" 
			
	}

}