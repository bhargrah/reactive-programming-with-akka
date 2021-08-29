package com.akka.services

import java.util.UUID

import com.akka.persistence.Model.User
import com.akka.persistence.UserDao
import com.akka.util.Util
import com.typesafe.config.Config
import org.slf4j.{Logger, LoggerFactory}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class UserServiceDBImpl(config: Config, userDao: UserDao) extends UserService {

  private val logger: Logger = LoggerFactory.getLogger(getClass.getName)

  override def all: Future[ServiceResponse[Seq[User]]] = {
    logger.info("[all]")

    userDao
      .all
      .map(users => Right(users))
      .recover {
        case e: Exception => Left(ErrorResponse(e.getMessage, 0))
      }
  }

  override def create(user: User): Future[ServiceResponse[User]] = {
    logger.info("[create] - {}", user.id)

    encryptPassword(user)
      .zip(isUsernameUnique(user))
      .flatMap { encUserAndIsUnique: (User, Boolean) =>

        val (pwdEncryptedUser, isUnameUnique) = encUserAndIsUnique
        if (isUnameUnique)
          userDao.insert(pwdEncryptedUser)
        else throw new IllegalArgumentException(s"Username ${pwdEncryptedUser.username} already exists.")
      }
      .map { created =>
        Right(created)
      }
      .recover {
        case e: Exception => Left(ErrorResponse(e.getMessage, 0))
      }

  }

  private def encryptPassword(user: User): Future[User] =
    Util
      .encrypt(user.password)
      .map(encrypted => user.copy(password = encrypted))

  private def isUsernameUnique(user: User): Future[Boolean] =
    userDao
      .byUsername(user.username)
      .map(_.isEmpty)


  override def byId(id: UUID): Future[ServiceResponse[User]] = {
    logger.info("[byId] - {}", id)

    userDao
      .byId(id)
      .map {
        case None => Left(ErrorResponse(s"Could not read User with Id: $id", 0))
        case Some(user) => Right(user)
      }
      .recover {
        case e: Exception =>
          logger.info("[create] - exception occurred: {}", e.getMessage)
          Left(ErrorResponse(e.getMessage, 0))
      }
  }

  override def delete(id: UUID): Future[ServiceResponse[Boolean]] = {
    logger.info("[delete] - {}", id)

    userDao
      .remove(id)
      .map {
        case true => Right(true)
        case false => Left(ErrorResponse("Could not delete User with Given ID", 0))
      }
      .recover {
        case e: Exception =>
          logger.info("[create] - exception occurred: {}", e.getMessage)
          Left(ErrorResponse(e.getMessage, 0))
      }
  }
}