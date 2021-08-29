package com.akka.fundamentals.actors.requests

sealed trait ToppingRequest

case object ExtraCheese extends ToppingRequest
case object Jalapeno extends ToppingRequest

