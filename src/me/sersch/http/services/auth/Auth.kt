package me.sersch.http.services.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.io.FileInputStream
import java.util.*

open class Auth(private val secret: String) {
    private val algorithm = Algorithm.HMAC256(secret)
    val verifier = JWT.require(algorithm).build()
    fun sign(name: String): String = JWT.create().withClaim("name", name).sign(algorithm)
}
