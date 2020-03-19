package me.sersch.http.components.user

import io.ktor.auth.UserIdPrincipal
import io.ktor.http.HttpStatusCode
import me.sersch.http.services.auth.AuthPayload
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.collections.ArrayList

class UserController {
    private val model: UserModelInterface = UserModel()

    constructor() {
        transaction {
            SchemaUtils.create(Users)
        }
    }

    fun getAll(): ArrayList<UserInterface> = model.getAllUsersFromRepository()

    fun getOne(id: Int): UserInterface = model.getOneUserFromRepository(id)

    fun getUserCredentials(mail: String): UserDTO = model.getUserCredentialsFromRepository(mail)

    fun insert(user: UserDTO) = model.insertUserToRepository(user)

    fun update(user: UserDTO, id: Int) = model.updateUserInRepository(user, id)

    fun delete(id: Int): HttpStatusCode = model.deleteUserInRepository(id)
}