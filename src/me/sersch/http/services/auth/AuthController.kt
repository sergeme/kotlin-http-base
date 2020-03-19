package me.sersch.http.services.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import io.ktor.auth.UserIdPrincipal
import me.sersch.http.components.user.UserDTO
import me.sersch.http.components.user.UserInterface
import me.sersch.http.components.user.roles

open class AuthController(private val secret: String) {
    private val authModel: AuthModelInterface = AuthModel(secret)

    val verifier: JWTVerifier = JWT.require(authModel.algorithm).build()

    fun validate(payload: AuthPayload, userCredentials: UserDTO): Boolean =
        authModel.validateUserCredentials(payload, userCredentials)

    //Private as it is not used in routes at the moment, can later be used to restrict routes to certain roles
    private fun hasRole(user: UserInterface, role: String): Boolean = (roles[user.role] == role)

    // used in routes to decide access
    fun isAdminOrSelectedUser(principal: UserIdPrincipal, id: Int): Boolean = hasRole(
        authModel.getUserbyUserIdPrincipal(principal),
        "admin"
    ) || authModel.getUserbyUserIdPrincipal(principal).id == id

    fun sign(name: String): String = authModel.createJWT(name)
}
