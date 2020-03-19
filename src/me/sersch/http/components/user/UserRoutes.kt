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
import me.sersch.http.services.auth.AuthController

fun Application.userRoutes(userController: UserController, authController: AuthController) {
    routing {
        //Routes requiring authentication
        authenticate {
            get("/users") {
                call.respond(userController.getAll())
            }

            get("/user/{id}") {
                val userId = call.parameters["id"]?.toInt()!!
                call.respond(userController.getOne(userId))
            }

            put("/user/{id}") {
                val userId = call.parameters["id"]?.toInt()!!
                val principal = call.principal<UserIdPrincipal>() ?: error("No principal")
                //Only allow access to admins or the user itself
                if (authController.isAdminOrSelectedUser(principal, userId)) {
                    val id = call.parameters["id"]?.toInt()!!
                    val userDTO = call.receive<UserDTO>()
                    userController.update(userDTO, id)
                    call.respond(HttpStatusCode.OK)
                } else {
                    call.respond(HttpStatusCode.Forbidden)
                }
            }

            delete("/user/{id}") {
                val userId = call.parameters["id"]?.toInt()!!
                val principal = call.principal<UserIdPrincipal>() ?: error("No principal")
                println(principal.name)
                if (authController.isAdminOrSelectedUser(principal, userId)) {
                    call.respond(userController.delete(userId))
                }
            }
        }

        //Public Routes
        post("/user") {
            val userDto = call.receive<UserDTO>()
            userController.insert(userDto)
            call.respond(HttpStatusCode.Created)
        }
    }
}