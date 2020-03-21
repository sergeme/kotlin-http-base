package me.sersch.http.components.user

import me.sersch.http.helpers.Helper

//Interface for User Class
interface UserInterface {
    fun getAllUsersFromRepository(): Helper.Response

    fun getOneUserFromRepository(id: Int): Helper.Response

    fun insertUserToRepository(user: UserDTO): Helper.Response

    fun updateUserInRepository(user: UserDTO, id: Int): Helper.Response

    fun deleteUserInRepository(id: Int): Helper.Response

    fun getRoleKey(lookupRole: String): Int
}