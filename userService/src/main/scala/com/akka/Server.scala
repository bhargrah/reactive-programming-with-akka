package com.akka

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Route
import akka.stream.Materializer
import com.akka.persistence.UserDaoImpl
import com.akka.routes.UserRoutes
import com.akka.services.UserServiceDBImpl
import com.softwaremill.macwire.wire
import com.typesafe.config.{Config, ConfigFactory}

object Server extends App {

  implicit val system: ActorSystem = ActorSystem()
  implicit val materializer: Materializer = Materializer(system)
  val userDaoImpl: UserDaoImpl = wire[UserDaoImpl]
  //  val userService: UserServiceImpl = wire[UserServiceImpl]
  val userService: UserServiceDBImpl = wire[UserServiceDBImpl]
  val userRoutes: UserRoutes = wire[UserRoutes]
  val serviceRoutes: Route = userRoutes.routes

  def config: Config = ConfigFactory.load()

  HttpService.run(config, serviceRoutes)
}