package me.sersch.http.components.user

import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.util.*
import kotlin.collections.ArrayList

class UserController {

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
                it[id] = UUID.randomUUID()
                it[mail] = user.mail
                it[firstname] = user.firstName
                it[lastname] = user.lastName
            }
        }
    }

    fun update(user: UserDTO, id: UUID) {
        transaction {
            Users.update({Users.id eq id}) {
                it[mail] = user.mail
                it[firstname] = user.firstName
                it[lastname] = user.lastName
            }
        }
    }

    fun updateHash(user: UserDTO, id: UUID) {
        //Cryptozeug
        transaction {
            Users.update({Users.id eq id}) {
                it[hash] = "asd"
            }
        }
    }

    fun delete(id: UUID) {
        transaction {
            Users.deleteWhere { Users.id eq id }
        }
    }
}