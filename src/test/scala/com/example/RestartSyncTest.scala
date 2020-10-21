package com.example

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import akka.actor.testkit.typed.scaladsl.BehaviorTestKit
import akka.actor.testkit.typed.scaladsl.Effects
import com.example.Restart.Boom
import akka.actor.testkit.typed.Effect.Watched
import akka.actor.typed.ActorRef
import com.example.Restart.ChildCommand

class RestartSyncTest extends AnyFlatSpec with Matchers {
  
  "Restart Example" should "start child on every restart" in {
    val childOnRestart = BehaviorTestKit(Restart())
    childOnRestart.expectEffect(Effects.spawned(Restart.child, "child"))

    childOnRestart.run(Boom)
    childOnRestart.expectEffectType[Watched[ActorRef[ChildCommand]]]
    childOnRestart.expectEffect(Effects.stopped("child"))
    childOnRestart.expectEffect(Effects.spawned(Restart.child,"child")) //FIXME this fails!
  }
}
