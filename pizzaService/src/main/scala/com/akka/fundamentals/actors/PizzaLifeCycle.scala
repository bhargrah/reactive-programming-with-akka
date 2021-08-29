package com.akka.fundamentals.actors

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}
import com.akka.fundamentals.actors.requests.{MargheritaRequest, MarinaraRequest, PizzaException}

/**
Description : This example illustrate lifecycle method for actors.
 **/

// This class extends Actor along with ActorLogging to get logger handler
class PizzaLifeCycle extends Actor with ActorLogging {

  override def preStart() = log.info("Pizza Request Received ")

  override def receive: Receive = {
    case MarinaraRequest => log.info("I have a Marinara request!")
    case MargheritaRequest => log.info("I have a Margherita request!")
    case PizzaException => throw new Exception("Pizza fried!")
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

object TestPizzaLifeCycleActor {
  def main(args: Array[String]): Unit = {

    val system = ActorSystem("PizzaSystem")
    val pizza: ActorRef =  system.actorOf(Props[PizzaLifeCycle], "PizzaLifeCycle")

    // Orchestration of load simulation
    pizza ! MarinaraRequest
    pizza ! PizzaException   // this will cause an exception in pizza workflow
    pizza ! MargheritaRequest
    pizza ! MarinaraRequest

    system.terminate()
  }

}
