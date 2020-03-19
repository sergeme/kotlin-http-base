package me.sersch.http.config

import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.UserIdPrincipal
import io.ktor.auth.jwt.jwt
import io.ktor.features.CORS
import io.ktor.features.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.jackson.jackson
import me.sersch.http.components.user.UserController
import me.sersch.http.components.user.userRoutes
import me.sersch.http.services.auth.AuthController
import me.sersch.http.services.auth.authRoute

//Ktor feature installation and configuration
fun Application.installFeatures(authController: AuthController) {
    install(CORS) {
        method(HttpMethod.Options)
        method(HttpMethod.Put)
        method(HttpMethod.Delete)
        method(HttpMethod.Patch)
        header(HttpHeaders.Authorization)
        header("MyCustomHeader")
        allowCredentials = true
        anyHost() // @TODO: Don't do this in production if possible. Try to limit it.
    }

    install(Authentication) {
        jwt {
            verifier(authController.verifier)
            validate {
                UserIdPrincipal(it.payload.getClaim("name").asString())
            }
        }
    }

    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }
}

//Ktor enabling routes and requirements
fun Application.enableRoutes(userController: UserController, authController: AuthController) {
    userRoutes(userController, authController)
    authRoute(userController, authController)
}