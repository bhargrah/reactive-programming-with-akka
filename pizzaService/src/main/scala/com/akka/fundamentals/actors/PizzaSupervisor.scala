package com.akka.fundamentals.actors

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import com.akka.fundamentals.actors.requests.{ExtraCheese, Jalapeno, MargheritaRequest, MarinaraRequest}

class PizzaToppings extends Actor{
  def receive = {
    case ExtraCheese => println("Aye! Extra cheese it is")
    case Jalapeno => println("More Jalapenos!")
  }
}

class PizzaSupervisor extends Actor {

  // child actor created
  val pizzaToppings =context.actorOf(Props[PizzaToppings], "PizzaToppings")

  def receive = {
    case MarinaraRequest   =>
      println("I have a Marinara request with extra cheese!")
      println(pizzaToppings.path)
      pizzaToppings ! ExtraCheese

    case MargheritaRequest =>
      println("I have a Margherita request!")

    case _    =>
      throw new Exception("Pizza fried!")
  }
}

  object TestActorPath {
   def main(args: Array[String]): Unit = {

     // actor system
    val system = ActorSystem("Pizza")

     // ref for actor , no direct interaction , kind of handle
    val pizza: ActorRef =  system.actorOf(Props[PizzaSupervisor], "PizzaSupervisor")

     println(pizza.path)

    pizza ! MarinaraRequest
    system.terminate()
  }

}
