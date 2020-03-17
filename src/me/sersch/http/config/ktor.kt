package me.sersch.http.config

import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.UserIdPrincipal
import io.ktor.auth.jwt.jwt
import io.ktor.features.CORS
import io.ktor.features.ContentNegotiation
import io.ktor.features.StatusPages
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.jackson.jackson
import io.ktor.serialization.DefaultJsonConfiguration
import io.ktor.serialization.serialization
import kotlinx.serialization.json.Json
import me.sersch.http.components.auth.loginRoute
import me.sersch.http.components.role.RoleController
import me.sersch.http.components.user.UserController
import me.sersch.http.components.user.userRoutes
import me.sersch.http.services.auth.Auth
import me.sersch.http.services.login.LoginController

//Ktor feature installation and configuration
fun Application.installFeatures(auth: Auth) {
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

    //@Todo replace secret, put it in file

    install(Authentication) {
        jwt {
            verifier(auth.verifier)
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
fun Application.enableRoutes(auth: Auth) {
    val userController = UserController()
    val roleController = RoleController()
    val loginController = LoginController()
    userController.setRoleController(roleController)
    userRoutes(userController)
    loginRoute(userController, loginController, auth)
}