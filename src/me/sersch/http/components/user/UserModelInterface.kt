package me.sersch.http.components.user

import io.ktor.auth.UserIdPrincipal
import io.ktor.http.HttpStatusCode
import me.sersch.http.services.auth.AuthPayload

//Interface for UserModel, to allow exposed implementation to be replaced
interface UserModelInterface {

    fun getAllUsersFromRepository(): ArrayList<UserInterface>

    fun getOneUserFromRepository(id: Int): UserInterface

    fun getUserCredentialsFromRepository(mail: String): UserDTO

    fun insertUserToRepository(user: UserDTO): HttpStatusCode

    fun updateUserInRepository(user: UserDTO, id: Int): HttpStatusCode

    fun deleteUserInRepository(id: Int): HttpStatusCode

    fun getRoleKey(lookupRole: String): Int
}