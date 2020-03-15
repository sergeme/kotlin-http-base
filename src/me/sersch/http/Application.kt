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
import org.jetbrains.exposed.dao.id.EntityID
import java.time.LocalDate

data class Model(val name: String, val items: List<Item>, val date: LocalDate = LocalDate.of(2018, 4, 13))
data class Item(val key: String, val value: String)

val model = Model("root", listOf(Item("A", "Apache"), Item("B", "Bing")))

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf

fun Application.module(testing: Boolean = false) {
    installFeatures()
    initDB()
    userRoutes()
    val roleController = RoleController()
}

