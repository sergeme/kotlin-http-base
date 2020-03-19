package me.sersch.http

import io.ktor.application.*
import me.sersch.http.components.user.UserController
import me.sersch.http.services.auth.AuthController
import me.sersch.http.config.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf

fun Application.module(testing: Boolean = false) {
    initDB()
    val userController = UserController()
    val authController = AuthController("2g6i3zhg42i54g234zoio72b35ob67u2oi354b")
    installFeatures(authController)
    enableRoutes(userController, authController)
}