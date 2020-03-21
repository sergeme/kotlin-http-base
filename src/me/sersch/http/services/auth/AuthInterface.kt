package me.sersch.http.services.auth

import com.auth0.jwt.algorithms.Algorithm
import io.ktor.auth.UserIdPrincipal
import me.sersch.http.components.user.UserDTO
import me.sersch.http.helpers.Helper

interface AuthInterface {

    val algorithm: Algorithm

    fun getUserbyUserIdPrincipal(principal: UserIdPrincipal): UserDTO

    fun getUserCredentialsFromRepository(mail: String): UserDTO

    fun validateUserCredentials(payload: AuthPayload, userCredentials: UserDTO): Helper.Response

    fun createJWT(name: String): String
}