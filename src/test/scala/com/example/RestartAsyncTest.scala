package com.example

import akka.actor.testkit.typed.scaladsl.ScalaTestWithActorTestKit
import org.scalatest.flatspec.AnyFlatSpecLike
import org.scalatest.matchers.should.Matchers
import akka.actor.testkit.typed.scaladsl.LoggingTestKit
import com.example.Restart.Boom

class RestartAsyncTest extends ScalaTestWithActorTestKit with AnyFlatSpecLike with Matchers {
  
  "Restart Example" should "start child on start and restart" in {
    val childOnStart = testKit.spawn(Restart())
    LoggingTestKit.info("creating child").expect {
      childOnStart.tell(Boom)
    }

  }
}
