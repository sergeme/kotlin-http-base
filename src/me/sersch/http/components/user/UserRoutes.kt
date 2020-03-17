package me.sersch.http.components.user

import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.authenticate
import io.ktor.features.CORS
import io.ktor.features.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.jackson.jackson
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*
import org.jetbrains.exposed.dao.id.EntityID

fun Application.userRoutes(userController: UserController) {
    routing {
        //Routes requiring authentication
        authenticate {
            get("/users") {
                call.respond(userController.getAll())
            }

            get("/user/{id}") {
                val id = call.parameters["id"]?.toInt()!!
                call.respond(userController.getOne(id))
            }

            put("/user/{id}") {
                val id = call.parameters["id"]?.toInt()!!
                val userDTO = call.receive<UserDTO>()
                userController.update(userDTO, id)
                call.respond(HttpStatusCode.OK)
            }

            delete("/user/{id}") {
                val id = call.parameters["id"]?.toInt()!!
                userController.delete(id)
                call.respond(HttpStatusCode.OK)
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