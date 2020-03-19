package me.sersch.http.components.user

import io.ktor.auth.UserIdPrincipal
import io.ktor.http.HttpStatusCode
import me.sersch.http.services.auth.AuthPayload
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.lang.Exception

//Business logic for /user and /users routes/endpoints
class UserModel : UserModelInterface {

    override fun getAllUsersFromRepository(): ArrayList<UserInterface> {
        val users: ArrayList<UserInterface> = arrayListOf()
        transaction {
            Users.selectAll().map {
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
        return users
    }

    override fun getOneUserFromRepository(id: Int): UserInterface {
        val users: ArrayList<User> = arrayListOf()
        transaction {
            Users.select { Users.id eq id }.map {
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

    override fun getUserCredentialsFromRepository(mail: String): UserDTO {
        val users: ArrayList<UserDTO> = arrayListOf()
        transaction {
            Users.select { Users.mail eq mail }.map {
                users.add(
                    UserDTO(
                        firstName = it[Users.firstname],
                        lastName = it[Users.lastname],
                        mail = it[Users.mail],
                        hash = it[Users.hash],
                        role = it[Users.role]
                    )
                )
            }
        }
        return UserDTO(
            firstName = users[0].firstName,
            lastName = users[0].lastName,
            mail = users[0].mail,
            hash = users[0].hash,
            role = users[0].role
        )
    }

    override fun insertUserToRepository(user: UserDTO): HttpStatusCode {
        var returnCode: HttpStatusCode = HttpStatusCode.OK
        if (transaction { Users.select { Users.mail eq user.mail }.toList().count() != 0 }) {
            returnCode = HttpStatusCode.Conflict
        } else {
            try {
                transaction {
                    //Give user the admin role if table is empty
                    val role: Int = if (Users.selectAll().toList().count() == 0) {
                        getRoleKey("admin")
                    } else {
                        getRoleKey("default")
                    }
                    Users.insert {
                        it[firstname] = user.firstName
                        it[lastname] = user.lastName
                        it[mail] = user.mail
                        it[hash] = user.hash
                        it[Users.role] = role
                    }
                }
            } catch (e: Exception) {
                println("SqlException: $e")
                returnCode = HttpStatusCode.InternalServerError
            }
        }
        return returnCode
    }

    override fun updateUserInRepository(user: UserDTO, id: Int): HttpStatusCode {
        var returnCode: HttpStatusCode = HttpStatusCode.OK
        if (transaction { Users.select { Users.mail eq user.mail }.toList().count() != 0 }) {
            returnCode = HttpStatusCode.Conflict
        }
        try {
            transaction {
                Users.update({ Users.id eq id }) {
                    it[firstname] = user.firstName
                    it[lastname] = user.lastName
                    it[mail] = user.mail
                    it[hash] = user.hash
                }
            }
        } catch (e: Exception) {
            println("SqlException: $e")
            returnCode = HttpStatusCode.InternalServerError
        }
        return returnCode
    }

    override fun deleteUserInRepository(id: Int): HttpStatusCode {
        var returnCode: HttpStatusCode = HttpStatusCode.OK
        try {
            val delete = transaction {
                Users.deleteWhere { Users.id eq id }
            }
            if (delete == 0) {
                returnCode = HttpStatusCode.Gone
            }
        } catch (e: Exception) {
            println("SqlException: $e")
            returnCode = HttpStatusCode.InternalServerError
        }
        return returnCode
    }

    override fun getRoleKey(lookupRole: String): Int {
        var foundRole: Int = 0
        roles.forEach(fun(index, role) {
            if (role == lookupRole) foundRole = index
        })
        return foundRole
    }
}