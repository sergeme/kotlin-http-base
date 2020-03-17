package me.sersch.http.components.auth

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*
import me.sersch.http.components.user.UserController
import me.sersch.http.services.auth.Auth
import me.sersch.http.services.auth.AuthPayload
import me.sersch.http.services.login.LoginController

fun Application.loginRoute(userController: UserController, loginController: LoginController, auth: Auth) {
    routing {
        //Routes requiring authentication
        post("/login") {
            val payload = call.receive<AuthPayload>()
            val user = userController.getUserCredentials(payload.userName)
            if(!loginController.validate(payload, user)) error("Invalid credentials")
            call.respond(mapOf<String, String>("token" to auth.sign(user.userName)))
        }
    }
}