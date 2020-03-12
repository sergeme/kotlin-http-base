package me.sersch.http

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import me.sersch.http.config.*


fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf

fun Application.module(testing: Boolean = false) {
    installFeatures()
    initDB()


    routing {
        get("/") {
            call.respond(mapOf("OK" to true))
        }
    }
}

