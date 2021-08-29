package com.akka.fundamentals.actors

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import com.akka.fundamentals.actors.requests.{MargheritaRequest, MarinaraRequest}

class PizzaChef extends Actor {
  def receive = {
    case MarinaraRequest => println("I have a Marinara request!")
    case MargheritaRequest => println("I have a Margherita request!")
  }
}

object PizzaCustomer{

  def main(args: Array[String]) : Unit = {

    val system = ActorSystem("Pizza")

    val pizzaChef: ActorRef = system.actorOf(Props[PizzaChef])

    pizzaChef ! MarinaraRequest
    pizzaChef ! MargheritaRequest
  }
}

