package me.sersch.http

import io.ktor.application.*
import me.sersch.http.components.user.UserController
import me.sersch.http.services.auth.AuthController
import me.sersch.http.config.*

//application entry point, runs the integrated netty server
fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

//no messages regarding modules that are configured in application.conf, but not implemented
@Suppress("unused")

//initializes the database, controllers, enables server features and routes
fun Application.module(testing: Boolean = false) {
    initDB()
    val authController = AuthController("2g6i3zhg42i54g234zoio72b35ob67u2oi354b")
    val userController = UserController(authController)
    installFeatures(authController)
    enableRoutes(userController, authController)
}