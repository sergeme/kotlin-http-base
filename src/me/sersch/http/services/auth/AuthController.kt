package me.sersch.http.services.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import io.ktor.auth.UserIdPrincipal
import me.sersch.http.components.user.UserDTO
import me.sersch.http.components.user.roles
import me.sersch.http.helpers.Helper

open class AuthController(private val secret: String) {
    private val auth: AuthInterface = Auth(secret)

    val verifier: JWTVerifier = JWT.require(auth.algorithm).build()

    //Sends submitted data and credentials from database to the model.
    fun validate(payload: AuthPayload, userCredentials: UserDTO): Helper.Response =
        auth.validateUserCredentials(payload, userCredentials)

    //Private as it is not used in routes at the moment, can later be used to restrict routes to certain roles
    private fun hasRole(user: UserDTO, role: String): Boolean = (roles[user.role] == role)

    //requests user credentials from the model
    fun getUserCredentials(mail: String): UserDTO = auth.getUserCredentialsFromRepository(mail)

    // used in routes to decide access
    fun isAdminOrSelectedUser(principal: UserIdPrincipal, id: Int): Boolean = hasRole(
        auth.getUserbyUserIdPrincipal(principal),
        "admin"
    ) || auth.getUserbyUserIdPrincipal(principal).id == id
}
