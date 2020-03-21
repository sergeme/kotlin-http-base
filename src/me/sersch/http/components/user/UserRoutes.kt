package me.sersch.http.components.user

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.auth.UserIdPrincipal
import io.ktor.auth.authenticate
import io.ktor.auth.principal
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*
import java.lang.Exception

fun Application.userRoutes(userController: UserController) {
    routing {
        //Routes requiring authentication
        authenticate {
            get("/users") {
                try {
                    val users = userController.getAll()
                    call.respond(users.responseCode, users.responseObject)
                } catch(e: Exception) {
                    call.respond(HttpStatusCode.BadRequest, "${e.message}")
                }
            }

            get("/user/{id}") {
                try {
                    val userId = call.parameters["id"]?.toInt()!!
                    val user = userController.getOne(userId)
                    call.respond(user.responseCode, user.responseObject)
                } catch(e: Exception) {
                    call.respond(HttpStatusCode.BadRequest, "${e.message}")
                }
            }

            put("/user/{id}") {
                try {
                    val principal = call.principal<UserIdPrincipal>() ?: error("No principal")
                    val id = call.parameters["id"]?.toInt()!!
                    val userDTO = call.receive<UserDTO>()
                    val response = userController.update(userDTO, id, principal)
                    call.respond(response.responseCode, response.responseObject)
                } catch(e: Exception) {
                    call.respond(HttpStatusCode.BadRequest, "${e.message}")
                }
            }

            delete("/user/{id}") {
                try {
                    val userId = call.parameters["id"]?.toInt()!!
                    val principal = call.principal<UserIdPrincipal>() ?: error("No principal")
                    val response = userController.delete(userId, principal)
                    call.respond(response.responseCode, response.responseCode)
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.BadRequest, "${e.message}")
                }
            }
        }

        //Public Routes
        post("/user") {
            try {
                val userDTO = call.receive<UserDTO>()
                userController.insert(userDTO)
                call.respond(HttpStatusCode.Created)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, "${e.message}")
            }
        }
    }
}