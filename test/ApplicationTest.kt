package me.sersch.http.base

import io.ktor.http.*
import kotlin.test.*
import io.ktor.server.testing.*
import me.sersch.http.module

class ApplicationTest {
    @Test
    fun testRoot() {
        withTestApplication({ module(testing = true) }) {
            handleRequest(HttpMethod.Get, "/").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals("HELLO WORLD!", response.content)
            }
        }
    }
}
