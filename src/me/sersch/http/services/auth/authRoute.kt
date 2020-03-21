package me.sersch.http.services.auth

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*
import java.lang.Exception

fun Application.authRoute(authController: AuthController) {
    routing {
        //Routes requiring authentication
        post("/auth") {
            try {
                val payload = call.receive<AuthPayload>()
                //Get user from database by sending username to controller
                val user = authController.getUserCredentials(payload.userName)
                //let controller handle the rest
                val response = authController.validate(payload, user)
                call.respond(response.responseCode, response.responseObject)

            } catch(e: Exception) {
                call.respond(HttpStatusCode.BadRequest, "${e.message}")
            }
        }
    }
}