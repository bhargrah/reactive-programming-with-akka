package com.akka.routes

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.akka.persistence.Model._
import com.akka.services.{UserService, _}
import org.slf4j.{Logger, LoggerFactory}

class UserRoutes(userService: UserService) {

  final val userAPI = "user"
  val logger: Logger = LoggerFactory.getLogger(getClass)
  val routes: Route =
    pathPrefix(userAPI) {
      concat(
        pathEndOrSingleSlash {
          concat(

            get {
              logger.info("GET all request for users")

              respondWith(userService.all)
            },

            (post & entity(as[User])) { user =>
              logger.info("POST request for create user with id {}", user.id)

              respondWith(userService.create(user))
            }
          )
        },
        pathPrefix(JavaUUID) { id =>
          concat(
            pathEndOrSingleSlash {
              concat(

                get {
                  logger.info("GET request for user with id {}", id)

                  respondWith(userService.byId(id))
                },

                delete {
                  logger.info("DELETE request for user with id {}", id)

                  respondWith(userService.delete(id))
                }
              )
            }
          )
        }
      )
    }

}