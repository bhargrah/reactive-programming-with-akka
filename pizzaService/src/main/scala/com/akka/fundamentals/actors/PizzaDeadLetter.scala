package com.akka.fundamentals.actors

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}
import com.akka.fundamentals.actors.requests.{MargheritaRequest, MarinaraRequest, PizzaException, StopPizzaBaking}

class PizzaDeadLetter extends Actor with ActorLogging {

  override def preStart() = log.info("Pizza Request Received ")

  override def receive: Receive = {
    case MarinaraRequest => log.info("I have a Marinara request!")
    case MargheritaRequest => log.info("I have a Margherita request!")
    case PizzaException => throw new Exception("Pizza fried!")
    case StopPizzaBaking => context.stop(self)
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
    pizza ! MargheritaRequest
    pizza ! MarinaraRequest

    system.terminate()
  }

}
