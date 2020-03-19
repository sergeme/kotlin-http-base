package me.sersch.http.services.auth

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*
import me.sersch.http.components.user.UserController

fun Application.authRoute(userController: UserController, authController: AuthController) {
    routing {
        //Routes requiring authentication
        post("/auth") {
            val payload = call.receive<AuthPayload>()
            val user = userController.getUserCredentials(payload.userName)
            if (!authController.validate(payload, user)) call.respond(HttpStatusCode.Unauthorized)
            call.respond(mapOf<String, String>("token" to authController.sign(user.mail)))
        }
    }
}