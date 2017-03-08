package org.liprudent.majiang.engine.game.impl

import org.liprudent.majiang.engine.round.State._
import org.liprudent.majiang.engine.round.Action._
import org.liprudent.majiang.engine.round.IRound
import org.liprudent.majiang.engine.tile.impl._
import org.liprudent.majiang.engine.tile._
import com.google.common.collect._
import org.liprudent.majiang.engine.player.IPlayer.Wind._
import org.liprudent.majiang.engine.player.impl.Player
import org.liprudent.majiang.engine.game.impl._
import org.liprudent.majiang.engine.round.impl.Round
import org.liprudent.majiang.engine.player.IPlayer
import org.scalatest.FlatSpec

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.mock.EasyMockSugar
import org.scalatest.fixture.FixtureFlatSpec

import scala.collection.JavaConversions._

class GameSpec extends FlatSpec with ShouldMatchers{
	
  class TestableRound(player:IPlayer) extends Round(player:IPlayer){
	 override def isEnd : Boolean = {true}  
  }
  
  class TestableGame(players:java.util.Set[IPlayer]) extends Game(players:java.util.Set[IPlayer]){
	  override def  instanciateRound(firstPlayer:IPlayer):IRound ={
	 	val round = new TestableRound(firstPlayer)
	 	round.addObserver(this)
	 	round
	  }
  }
  
  "A game" should "reinstantiate a new round when previous round ended" in {
    val players :java.util.Set[org.liprudent.majiang.engine.player.IPlayer]  = Sets.newHashSet(Player.instanceOf("PLAYER_1"), Player.instanceOf("PLAYER_2"), Player.instanceOf("PLAYER_3"),Player.instanceOf("PLAYER_4"))
    val game = new TestableGame(players)

    //the old round
    val round = game.getLastRound
    //end the round
    round.doEnd
    // a new round should has been instanciated
    round should not equal (game.getLastRound)
    //and the dealer is the next player
    round.getCurrentPlayer
    
  }
  
  it should "be deserializable" in {
	  import java.io._ 
	  val players :java.util.Set[org.liprudent.majiang.engine.player.IPlayer]  = Sets.newHashSet(Player.instanceOf("PLAYER_1"), Player.instanceOf("PLAYER_2"), Player.instanceOf("PLAYER_3"),Player.instanceOf("PLAYER_4"))
	   val game = new Game(players)
	   val expectedNbEventsP1 = game.getEvents("PLAYER_1").nbUnread
	   val expectedNbEventsP3 = game.getEvents("PLAYER_3").nbUnread
	   val expectedNbEventsP2 = game.getEvents("PLAYER_2").nbUnread
	   val expectedNbEventsP4 = game.getEvents("PLAYER_4").nbUnread
	  game.getLastRound.asInstanceOf[java.util.Observable].countObservers should equal(1)
      game.findPlayer("PLAYER_1").asInstanceOf[java.util.Observable].countObservers should equal(1)
      game.findPlayer("PLAYER_2").asInstanceOf[java.util.Observable].countObservers should equal(1)
      game.findPlayer("PLAYER_3").asInstanceOf[java.util.Observable].countObservers should equal(1)
      game.findPlayer("PLAYER_4").asInstanceOf[java.util.Observable].countObservers should equal(1)
	   
	  //serialize the game
	  val fos = new FileOutputStream("game.ser");
      val out = new ObjectOutputStream(fos);
      out.writeObject(game);
      out.close();
      
      //deserialize the game
      val fis = new FileInputStream("game.ser");
      val in = new ObjectInputStream(fis);
      val gameD = in.readObject().asInstanceOf[Game];
      in.close();
      
      gameD should not equal(null)
      gameD.getEvents("PLAYER_1") should not be (null)
      gameD.getLastRound.asInstanceOf[java.util.Observable].countObservers should equal(1)
      gameD.findPlayer("PLAYER_1").asInstanceOf[java.util.Observable].countObservers should equal(1)
      gameD.findPlayer("PLAYER_2").asInstanceOf[java.util.Observable].countObservers should equal(1)
      gameD.findPlayer("PLAYER_3").asInstanceOf[java.util.Observable].countObservers should equal(1)
      gameD.findPlayer("PLAYER_4").asInstanceOf[java.util.Observable].countObservers should equal(1)
      gameD.getEvents("PLAYER_1") should not be (null)
      expectedNbEventsP3 should equal(gameD.getEvents("PLAYER_3").nbUnread)
      expectedNbEventsP2 should equal(gameD.getEvents("PLAYER_2").nbUnread)
      expectedNbEventsP4 should equal(gameD.getEvents("PLAYER_4").nbUnread)
      expectedNbEventsP1 should equal(gameD.getEvents("PLAYER_1").nbUnread)
  }
  
  it should "Stop when 4 rounds have been played" in {
	   val players :java.util.Set[org.liprudent.majiang.engine.player.IPlayer]  = Sets.newHashSet(Player.instanceOf("PLAYER_1"), Player.instanceOf("PLAYER_2"), Player.instanceOf("PLAYER_3"),Player.instanceOf("PLAYER_4"))
	    val game = new TestableGame(players)
	
	    //the old round
	    var round = game.getLastRound
	    var currentPlayer = round.getCurrentPlayer
	    //end the round
	    round.doEnd
	    
	    // round 2 has been instanciated
	    round should not equal (game.getLastRound)
	    round = game.getLastRound
	     //and the dealer is the next player
	    round.getCurrentPlayer should equal(currentPlayer.getNext)
	    currentPlayer = round.getCurrentPlayer
	    //end the round
	    round.doEnd
	    
	    //round 3 has been instanciated
	    round should not equal (game.getLastRound)
	    round = game.getLastRound
	     //and the dealer is the next player
	    round.getCurrentPlayer should equal(currentPlayer.getNext)
	    currentPlayer = round.getCurrentPlayer
	    //end the round
	    round.doEnd
	    
	    //round 4 has been instanciated
	    round should not equal (game.getLastRound)
	    round = game.getLastRound
	     //and the dealer is the next player
	    round.getCurrentPlayer should equal(currentPlayer.getNext)
	    currentPlayer = round.getCurrentPlayer
	    //end the round
	    round.doEnd
	    
	    //no new round is instanciated
	    round should equal (game.getLastRound)
	    round.getCurrentPlayer should equal(currentPlayer)
  }
  
}
