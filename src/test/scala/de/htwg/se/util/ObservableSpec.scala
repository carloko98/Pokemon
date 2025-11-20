package de.htwg.se.util

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers._

class ObservableSpec extends AnyWordSpec {

  // Hilfsklasse für den Test
  class TestObserver extends Observer {
    var updated: Boolean = false
    override def update(): Unit = updated = true
  }

  "An Observable" should {
    "add an Observer" in {
      val observable = new Observable
      val observer = new TestObserver
      observable.add(observer)
      observable.subscribers should contain(observer)
    }

    "remove an Observer" in {
      val observable = new Observable
      val observer = new TestObserver
      
      observable.add(observer)
      observable.remove(observer) 
      
      observable.subscribers should not contain(observer)
    }

    "notify Observers" in {
      val observable = new Observable
      val observer1 = new TestObserver
      val observer2 = new TestObserver
      
      observable.add(observer1)
      observable.add(observer2)
      
      observable.remove(observer2) 

      observable.notifyObservers()

      observer1.updated should be(true)  
      observer2.updated should be(false) 
    }
  }
}