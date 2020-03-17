package me.sersch.http.components.user

import me.sersch.http.components.role.RoleController
import me.sersch.http.components.role.roles
import me.sersch.http.services.auth.AuthPayload
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.lang.reflect.Array.set
import kotlin.collections.ArrayList

class UserController {
    private lateinit var roleController: RoleController
    constructor() {
        transaction {
            SchemaUtils.create (Users)
        }
    }

    fun setRoleController(roleController: RoleController) {
        this.roleController = roleController
    }

    fun getAll(): ArrayList<UserAPI> {
        val users: ArrayList<UserAPI> = arrayListOf()
        transaction {
            Users.selectAll().map {
                users.add(
                    UserAPI(
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

    fun getOne(id: Int): UserInterface {
        val users: ArrayList<UserAPI> = arrayListOf()
        transaction {
            Users.select{ Users.id eq id }.map {
                users.add(
                    UserAPI(
                        id = it[Users.id],
                        firstName = it[Users.firstname],
                        lastName = it[Users.lastname],
                        mail = it[Users.mail],
                        role = it[Users.role]
                    )
                )
            }
        }
        return object :UserInterface{
            val id = users[0].id
            override val firstName = users[0].firstName
            override val lastName = users[0].lastName
            override val mail = users[0].mail
            override val role = users[0].role
        }
    }

    fun getUserCredentials(mail: String) : AuthPayload {
        val users: ArrayList<AuthPayload> = arrayListOf()
        transaction {
            Users.select{ Users.mail eq mail }.map {
                users.add(
                    AuthPayload(
                        userName = it[Users.mail],
                        passwordHash = it[Users.hash]
                    )
                )
            }
        }
        return AuthPayload(userName = users[0].userName, passwordHash = users[0].passwordHash)
    }

    fun insert(user: UserDTO) {
        transaction {
            //Give user the admin role if table is empty
            val role: Int = if(Users.selectAll().toList().count() == 0) {
                roleController.getKey("admin")
            } else {
                roleController.getKey("default")
            }
            Users.insert {
                it[firstname] = user.firstName
                it[lastname] = user.lastName
                it[mail] = user.mail
                it[hash] = user.hash
                it[Users.role] = role
            }
        }
    }

    fun test(user: UserDTO) {
        transaction {
            println()
        }
    }

    fun update(user: UserDTO, id: Int) {
        transaction {
            Users.update({Users.id eq id}) {
                it[firstname] = user.firstName
                it[lastname] = user.lastName
                it[mail] = user.mail
                it[hash] = user.hash
            }
        }
    }

    fun delete(id: Int) {
        transaction {
            Users.deleteWhere { Users.id eq id }
        }
    }
}