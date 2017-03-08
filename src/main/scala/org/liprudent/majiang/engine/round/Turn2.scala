//package org.liprudent.majiang.engine.round.impl
//
//import org.liprudent.majiang.engine.round.impl.treatment.ITreatment
//import org.liprudent.majiang.engine.event.KindOfWall;
//import org.liprudent.majiang.engine.player.IPlayer
//
//import org.liprudent.majiang.engine.round.State._
//import org.liprudent.majiang.engine.tile.ITile
//import org.liprudent.majiang.engine.round.impl.treatment.ITreatmentHaveANewTile;
//import org.liprudent.majiang.engine.round.impl.treatment.IEatAction
//
///**
// * @param currentPlayer
// * @param newTile
// * @param source
// *
// */
//class Turn2(val currentPlayer: IPlayer, val newTile: ITile, val source: KindOfWall) {
//
//  /**
//   * They are players who can pung, kong, chow or mahjong after currentPlayer had discarded a tile
//   */
//  val actionPlayers: List[IPlayer] = List(currentPlayer.getNext, currentPlayer.getNext.getNext, currentPlayer.getNext.getNext.getNext)
//
//  /**
//   * count the number of treatment performed.
//   * When this value is 4, then it's the end of the turn
//   */
//  var nbTreatments = 0
//
//  /**
//   * Several actionPlayers may choose to chow, pung, kong or mahjong after currentPlayer has discarded his tile. 
//   * In this case, we must select the "strongest" eat action and give the discarded tile. 
//   * The purpose of this list is to collect all eat actions and at the end of the turn select the strongest
//   */
//  var eatActionTreatments: List[IEatAction] = Nil
//
//  /*
//   * constructor:
//   */
//  //1. give new tile to current player
//  currentPlayer.giveTileInConcealedHand(newTile, source)
//  //2. change state
//  currentPlayer.setState(HAVE_A_NEW_TILE)
//  actionPlayers.foreach(p => p.setState(WAIT_TILE_THROWED))
//
//  /**
//   * This is the main entry point of this class
//   */
//  def processTreatment(treatment: ITreatment) = {
//    //arg must not be null
//    require(treatment != null)
//
//    if (treatment.valid) {
//      nbTreatments = nbTreatments + 1
//      if (treatment.isInstanceOf[IEatAction]) {
//        //add the treatment to the list of eatActions
//        eatActionTreatments = treatment.asInstanceOf[IEatAction] :: eatActionTreatments
//        //set player state to END
//        treatment.getPlayer.setState(END)
//      }
//    } else {
//      if (!treatment.isInstanceOf[ITreatmentHaveANewTile]) {
//        nbTreatments = nbTreatments + 1
//        //set player state to END
//        treatment.getPlayer.setState(END)
//      }
//    }
//    
//
//  }
//
//  /**
//   * @return true if it's the end of the turn
//   */
//  def endTurn: Boolean = { false }
//
//  /**
//   * @return true if it's the end of the round
//   */
//  def endRound: Boolean = { false }
//
//  /**
//   * @return true if the turn winner won with a kong
//   */
//  def isKongTurnWinner: Boolean = { false }
//
//  /**
//   * @return The "turn winner" is the player who eats the discarded tile. None
//   * otherwise (everybody pass).
//   */
//  def getTurnWinner: Option[IPlayer] = { None }
//
//}