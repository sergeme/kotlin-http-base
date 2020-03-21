package me.sersch.http.services.auth

import at.favre.lib.crypto.bcrypt.BCrypt
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.auth.UserIdPrincipal
import io.ktor.http.HttpStatusCode
import me.sersch.http.components.user.UserDTO
import me.sersch.http.components.user.Users
import me.sersch.http.helpers.Helper
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

class Auth(secret: String) : AuthInterface {

    override val algorithm: Algorithm = Algorithm.HMAC256(secret)

    //Select one user by submitted Principal(Authentication)
    override fun getUserbyUserIdPrincipal(principal: UserIdPrincipal): UserDTO {
        val users: ArrayList<UserDTO> = arrayListOf()
        transaction {
            Users.select { Users.mail eq principal.name }.map {
                users.add(
                    UserDTO(
                        id = it[Users.id],
                        firstName = it[Users.firstname],
                        lastName = it[Users.lastname],
                        mail = it[Users.mail],
                        role = it[Users.role],
                        hash = it[Users.hash]
                    )
                )
            }
        }
        return users[0]
    }
    //Selects one user by e-mail
    override fun getUserCredentialsFromRepository(mail: String): UserDTO {
        val users: ArrayList<UserDTO> = arrayListOf()
        transaction {
            Users.select { Users.mail eq mail }.map {
                users.add(
                    UserDTO(
                        id = it[Users.id],
                        firstName = it[Users.firstname],
                        lastName = it[Users.lastname],
                        mail = it[Users.mail],
                        hash = it[Users.hash],
                        role = it[Users.role]
                    )
                )
            }
        }
        return users[0]
    }
    //Compare submitted user credential with record in database
    override fun validateUserCredentials(payload: AuthPayload, userCredentials: UserDTO): Helper.Response {
        var response = Helper.Response(HttpStatusCode.Unauthorized, "Authentication failure")
        if (BCrypt.verifyer().verify(payload.password.toByteArray(), userCredentials.hash.toByteArray()).verified) {
            response = Helper.Response(HttpStatusCode.OK, mapOf<String, String>("token" to createJWT(userCredentials.mail)))
        }
        return response
    }
    //Create JWT token for submitted value
    override fun createJWT(name: String): String = JWT.create().withClaim("name", name).sign(algorithm)
}