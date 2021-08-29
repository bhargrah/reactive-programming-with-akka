package com.akka.fundamentals.actors

import akka.actor.{Actor, ActorLogging, ActorSystem, Props, Terminated}
import com.akka.fundamentals.actors.requests.{MargheritaRequest, MarinaraRequest, PizzaException, PoisonPill}

/**
Description : This example illustrate how we can configure a watcher actor for any other actor created
 **/

class PizzaActor extends Actor with ActorLogging {

  override def preStart() = {
    log.info("Pizza request received!")
  }

  def receive = {
    case MarinaraRequest => log.info("I have a Marinara request!")
    case MargheritaRequest => log.info("I have a Margherita request!")
    case PizzaException => throw new Exception("Pizza fried!")
    case PoisonPill => context.stop(self)
  }

  override def preRestart(reason: Throwable, message: Option[Any]) = {
    log.info("Pizza baking restarted because " + reason.getMessage)
    postStop()
  }

  override def postRestart(reason: Throwable) = {
    log.info("New Pizza process started because " + reason.getMessage)
    preStart()
  }

  override def postStop() = {
    log.info("Pizza request finished")
  }

}

class PizzaWatcher extends Actor with ActorLogging {

  val pizza = context.actorOf(Props[PizzaActor], "pizzaactor")
  context.watch(pizza)

  def receive = {
    case "Stop" => pizza ! PoisonPill
    case Terminated(terminatedActorRef) => log.error(s"Watcher - ${terminatedActorRef} terminated")
  }
}

object PizzaWatcherTest {
  def main(args: Array[String]): Unit = {

    val system = ActorSystem("Pizza")
    val pizza = system.actorOf(Props[PizzaActor], "pizzaactor")

    val pizzaWatcher = system.actorOf(Props[PizzaWatcher], "pizzawatcher")

    pizza ! MarinaraRequest
    pizzaWatcher ! "Stop"

  }
}
