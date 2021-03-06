package com.akka.practice

import akka.actor.{Actor, ActorRef, Props, ActorSystem}

sealed trait PizzaRequest
case object MarinaraRequest extends PizzaRequest
case object MargheritaRequest extends PizzaRequest


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

