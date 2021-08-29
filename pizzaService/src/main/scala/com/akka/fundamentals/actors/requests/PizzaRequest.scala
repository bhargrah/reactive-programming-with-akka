package com.akka.fundamentals.actors.requests

sealed trait PizzaRequest

case object MarinaraRequest extends PizzaRequest
case object MargheritaRequest extends PizzaRequest
case object PizzaException extends PizzaRequest
case object StopPizzaBaking extends PizzaRequest
case object PoisonPill extends PizzaRequest