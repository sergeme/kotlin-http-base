package me.sersch.http.services.auth

import com.auth0.jwt.algorithms.Algorithm
import io.ktor.auth.UserIdPrincipal
import me.sersch.http.components.user.UserDTO
import me.sersch.http.components.user.UserInterface

interface AuthModelInterface {

    val algorithm: Algorithm

    fun getUserbyUserIdPrincipal(principal: UserIdPrincipal): UserInterface

    fun validateUserCredentials(payload: AuthPayload, userCredentials: UserDTO): Boolean

    fun createJWT(name: String): String
}