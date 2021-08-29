package com.akka.fundamentals.actors

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import com.akka.fundamentals.actors.requests.{MargheritaRequest, MarinaraRequest}

/**
 Description : This example illustrate how to create a simple Actor and to call Tell via ! and its interaction.
 **/

// Simple class which extends from Actor
class PizzaChef extends Actor {
  def receive = {
    case MarinaraRequest => println("I have a Marinara request!")
    case MargheritaRequest => println("I have a Margherita request!")
  }
}

object PizzaCustomer{

  def main(args: Array[String]) : Unit = {

    val system = ActorSystem("Pizza") // create system actor

    val pizzaChef: ActorRef = system.actorOf(Props[PizzaChef]) // get actor  ref

    pizzaChef ! MarinaraRequest // calling tell via !

    pizzaChef.tell(MargheritaRequest,pizzaChef) // calling actor via explicit tell method
  }
}

