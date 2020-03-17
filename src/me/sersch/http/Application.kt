package me.sersch.http

import io.ktor.application.*
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.*
import io.ktor.routing.*
import me.sersch.http.config.*
import me.sersch.http.components.user.UserController
import me.sersch.http.components.role.RoleController
import me.sersch.http.components.user.UserDTO
import me.sersch.http.components.user.Users
import me.sersch.http.components.user.userRoutes
import me.sersch.http.services.auth.Auth
import org.jetbrains.exposed.dao.id.EntityID
import java.time.LocalDate

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf

fun Application.module(testing: Boolean = false) {
    val auth = Auth("2g6i3zhg42i54g234zoio72b35ob67u2oi354b")
    installFeatures(auth)
    initDB()
    enableRoutes(auth)
}

