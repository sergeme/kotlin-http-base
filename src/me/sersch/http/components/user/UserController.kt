package me.sersch.http.components.user

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.collections.ArrayList

class UserController {

    constructor() {
        transaction {
            SchemaUtils.create (Users)
        }
    }

    fun getAll(): ArrayList<User> {
        val users: ArrayList<User> = arrayListOf()
        transaction {
            Users.selectAll().map {
                users.add(
                    User(
                        id = it[Users.id],
                        firstName = it[Users.firstname],
                        lastName = it[Users.lastname],
                        mail = it[Users.mail],
                        hash = it[Users.hash]
                    )
                )
            }
        }
        return users
    }

    fun insert(user: UserDTO) {
        transaction {
            Users.insert {
                it[firstname] = user.firstName
                it[lastname] = user.lastName
                it[mail] = user.mail
                it[hash] = user.hash
            }
        }
    }

    fun update(user: UserDTO, id: EntityID<Int>) {
        transaction {
            Users.update({Users.id eq id}) {
                it[firstname] = user.firstName
                it[lastname] = user.lastName
                it[mail] = user.mail
                it[hash] = user.hash
            }
        }
    }

    fun delete(id: EntityID<Int>) {
        transaction {
            Users.deleteWhere { Users.id eq id }
        }
    }
}