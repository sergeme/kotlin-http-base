package me.sersch.http.components.user

import io.ktor.auth.UserIdPrincipal
import io.ktor.http.HttpStatusCode
import me.sersch.http.helpers.Helper
import me.sersch.http.services.auth.AuthController

class UserController {
    private val userModel: UserInterface = User()
    private lateinit var authController: AuthController

    constructor(authController: AuthController) {
        setAuthController(authController)
    }

    private fun setAuthController(authController: AuthController) {
        this.authController = authController
    }

    fun getAll(): Helper.Response = userModel.getAllUsersFromRepository()

    fun getOne(id: Int): Helper.Response = userModel.getOneUserFromRepository(id)

    fun insert(user: UserDTO): Helper.Response = userModel.insertUserToRepository(user)

    fun update(user: UserDTO, id: Int, principal: UserIdPrincipal): Helper.Response {
        return if (authController.isAdminOrSelectedUser(principal, id)) {
            userModel.updateUserInRepository(user, id)
        } else {
            Helper.Response(HttpStatusCode.Forbidden, "Action not permitted")
        }
    }

    fun delete(id: Int, principal: UserIdPrincipal): Helper.Response {
        return if (authController.isAdminOrSelectedUser(principal, id)) {
            userModel.deleteUserInRepository(id)
        } else {
            Helper.Response(HttpStatusCode.Forbidden, "Action not permitted")
        }
    }
}