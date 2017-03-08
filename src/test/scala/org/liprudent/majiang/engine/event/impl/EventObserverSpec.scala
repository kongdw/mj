package org.liprudent.majiang.engine.event.impl

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

class EventObserverSpec extends FlatSpec with ShouldMatchers {

  "A EventObserver" should "be serializable" in {
    val p1 = Player.instanceOf("PLAYER_1")
    val eventObserver = new EventObserver(Sets.newHashSet(p1, Player.instanceOf("PLAYER_2"), Player.instanceOf("PLAYER_3"), Player.instanceOf("PLAYER_4")))
    eventObserver.update(p1, new org.liprudent.majiang.engine.event.impl.WindEvent("PLAYER_1", org.liprudent.majiang.engine.player.IPlayer.Wind.EAST))
    val expectedNbEvents = eventObserver.eventLogger("PLAYER_1").nbUnread
    import java.io._

    //serialize
    val fos = new FileOutputStream("EventObserverSpec.ser");
    val out = new ObjectOutputStream(fos);
    out.writeObject(eventObserver);
    out.close();

    //deserialize 
    val fis = new FileInputStream("EventObserverSpec.ser");
    val in = new ObjectInputStream(fis);
    val eventObserverD = in.readObject().asInstanceOf[EventObserver];
    in.close();

    eventObserverD.eventLogger("PLAYER_1") should not be (null)
    expectedNbEvents should equal(eventObserverD.eventLogger("PLAYER_1").nbUnread)
  }

  
  it should "be serializable when nested" in {
    val p1 = Player.instanceOf("PLAYER_1")
    val eventObserver = new EventObserver(Sets.newHashSet(p1, Player.instanceOf("PLAYER_2"), Player.instanceOf("PLAYER_3"), Player.instanceOf("PLAYER_4")))
    eventObserver.update(p1, new org.liprudent.majiang.engine.event.impl.WindEvent("PLAYER_1", org.liprudent.majiang.engine.player.IPlayer.Wind.EAST))
    val expectedNbEvents = eventObserver.eventLogger("PLAYER_1").nbUnread
    import java.io._
    val bulk = new Bulk(eventObserver)

    //serialize
    val fos = new FileOutputStream("EventObserverSpecBulk.ser");
    val out = new ObjectOutputStream(fos);
    out.writeObject(bulk);
    out.close();

    //deserialize 
    val fis = new FileInputStream("EventObserverSpecBulk.ser");
    val in = new ObjectInputStream(fis);
    val bulkD = in.readObject().asInstanceOf[Bulk];
    in.close();
    val eventObserverD = bulk.eo
    eventObserverD.eventLogger("PLAYER_1") should not be (null)
    eventObserverD.eventLogger("PLAYER_2") should not be (null)
    eventObserverD.eventLogger("PLAYER_3") should not be (null)
    eventObserverD.eventLogger("PLAYER_4") should not be (null)
    expectedNbEvents should equal(eventObserverD.eventLogger("PLAYER_1").nbUnread)
  }

}
@serializable
  class Bulk(val eo: EventObserver) {
  }