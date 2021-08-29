package com.akka.fundamentals.actors

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}
import com.akka.fundamentals.actors.requests.{MargheritaRequest, MarinaraRequest, PizzaException, StopPizzaBaking}

/**
Description : This example illustrate what happens when a actor dies and incoming messages moves to dead letter
 **/

class PizzaDeadLetter extends Actor with ActorLogging {

  override def preStart() = log.info("Pizza Request Received ")

  override def receive: Receive = {
    case MarinaraRequest => log.info("I have a Marinara request!")
    case MargheritaRequest => log.info("I have a Margherita request!")
    case PizzaException => throw new Exception("Pizza fried!")
    case StopPizzaBaking => context.stop(self) // this will stop the actor
  }

  override def preRestart(reason: Throwable, message: Option[Any]) = {
    log.info("Pizza baking restarted because " + reason.getMessage)
    postStop()
  }

  override def postRestart(reason: Throwable) = {
    log.info("New Pizza process started because earlier " + reason.getCause)
    preStart()
  }

  override def postStop() = log.info("Pizza Request Finished ")
}

object TestPizzaDeadLetterActor {
  def main(args: Array[String]): Unit = {

    val system = ActorSystem("PizzaSystem")
    val pizza: ActorRef =  system.actorOf(Props[PizzaDeadLetter], "PizzaDeadLetter")

    pizza ! MarinaraRequest
    pizza ! StopPizzaBaking
    pizza ! MargheritaRequest // will be moved to dead letter as actor is dead
    pizza ! MarinaraRequest // will be moved to dead letter as actor is dead

    system.terminate()
  }

}
