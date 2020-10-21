package com.example

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.SupervisorStrategy
import akka.actor.typed.scaladsl.ActorContext
import akka.actor.typed.ActorRef

object Restart {
  sealed trait Command
  case object DoThings extends Command
  case object Boom     extends Command

  def apply(): Behavior[Command] =
    Behaviors
      .supervise[Command] {
        Behaviors.setup { context =>
          // This code will be executed on Start and Restart
          context.log.info("Start & Restart")

          createChildren(context)
          Behaviors.receiveMessage {
            case DoThings =>
              //Do things
              Behaviors.same
            case Boom => throw new RuntimeException("BD problem")
          }
        }
      }
      .onFailure(SupervisorStrategy.restart)

  private def createChildren(context: ActorContext[Command]): ActorRef[ChildCommand] = {
    context.log.info("creating child")
    context.spawn(child, "child")
  }

  sealed trait ChildCommand
  case object NoOp extends ChildCommand

  val child: Behavior[ChildCommand] =
    Behaviors.receiveMessage {
      case NoOp => Behaviors.same
    }
}