package me.sersch.http.services.auth

import at.favre.lib.crypto.bcrypt.BCrypt
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.auth.UserIdPrincipal
import me.sersch.http.components.user.User
import me.sersch.http.components.user.UserDTO
import me.sersch.http.components.user.UserInterface
import me.sersch.http.components.user.Users
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

class AuthModel(secret: String) : AuthModelInterface {

    override val algorithm: Algorithm = Algorithm.HMAC256(secret)

    override fun getUserbyUserIdPrincipal(principal: UserIdPrincipal): UserInterface {
        val users: ArrayList<UserInterface> = arrayListOf()
        transaction {
            Users.select { Users.mail eq principal.name }.map {
                users.add(
                    User(
                        id = it[Users.id],
                        firstName = it[Users.firstname],
                        lastName = it[Users.lastname],
                        mail = it[Users.mail],
                        role = it[Users.role]
                    )
                )
            }
        }
        return User(
            id = users[0].id,
            firstName = users[0].firstName,
            lastName = users[0].lastName,
            mail = users[0].mail,
            role = users[0].role
        )
    }

    override fun validateUserCredentials(payload: AuthPayload, userCredentials: UserDTO): Boolean {
        return BCrypt.verifyer().verify(payload.password.toByteArray(), userCredentials.hash.toByteArray()).verified
    }

    override fun createJWT(name: String): String = JWT.create().withClaim("name", name).sign(algorithm)
}