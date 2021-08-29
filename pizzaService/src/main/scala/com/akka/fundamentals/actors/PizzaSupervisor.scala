package com.akka.fundamentals.actors

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import com.akka.fundamentals.actors.requests.{ExtraCheese, Jalapeno, MargheritaRequest, MarinaraRequest, PizzaException}

/**
Description : This example illustrate how we can define a supervisor and child actor delegation
 **/

// Child Actor
class PizzaToppings extends Actor{
  def receive = {
    case ExtraCheese => println("Aye! Extra cheese it is")
    case Jalapeno => println("More Jalapenos!")
  }
}

// Supervisor Actor
class PizzaSupervisor extends Actor {

  // Child actor created
  val pizzaToppings =context.actorOf(Props[PizzaToppings], "PizzaToppings")

  def receive = {
    case MarinaraRequest   =>
      println("I have a Marinara request with extra cheese!")
      println(pizzaToppings.path)
      pizzaToppings ! ExtraCheese

    case MargheritaRequest => println("I have a Margherita request!")

    case PizzaException => throw new Exception("Pizza fried!")
  }
}

  object TestActorPath {
   def main(args: Array[String]): Unit = {

    val system = ActorSystem("Pizza")      // actor system

     // ref for actor , no direct interaction , kind of handle
    val pizza: ActorRef =  system.actorOf(Props[PizzaSupervisor], "PizzaSupervisor")

     println(pizza.path) // prints the actor system paths

    pizza ! MarinaraRequest
    system.terminate()
  }

}
