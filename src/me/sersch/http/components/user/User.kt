package me.sersch.http.components.user

import io.ktor.http.HttpStatusCode
import me.sersch.http.helpers.Helper
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.lang.Exception

//Class that gets returned, sensitive data excluded
class User :UserInterface {

    constructor() {
        transaction {
            SchemaUtils.create(Users)
        }
    }

    override fun getAllUsersFromRepository(): Helper.Response {
        val users: ArrayList<UserAPI> = arrayListOf()
        try {
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
        } catch(e: Exception) {
            return Helper.Response(HttpStatusCode.InternalServerError, "Could not retrieve users")
        }
        return Helper.Response(HttpStatusCode.OK, users)
    }

    override fun getOneUserFromRepository(id: Int): Helper.Response {
        val users: ArrayList<UserAPI> = arrayListOf()
        try {
            transaction {
                Users.select { Users.id eq id }.map {
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
        } catch(e: Exception) {
            println("SqlException: $e")
            return Helper.Response(HttpStatusCode.InternalServerError, "Could not retrieve user")
        }
        return if(users.count() > 0) {
            Helper.Response(HttpStatusCode.OK, users[0])
        } else {
            Helper.Response(HttpStatusCode.NotFound, "Could not find user")
        }
    }

    private fun getUserHash(user: UserDTO): String {
        var returnStr = String()
        transaction {
            Users.select { Users.mail eq user.mail }.limit(1).map {
                returnStr = it[Users.hash]
            }
        }
        return returnStr
    }

    override fun insertUserToRepository(user: UserDTO): Helper.Response {
        var returnObject = Helper.Response(HttpStatusCode.Created, "User inserted into database")
        //Check if E-Mail in use
        if (transaction { Users.select { Users.mail eq user.mail }.toList().count() != 0 }) {
            returnObject = Helper.Response(HttpStatusCode.Conflict, "E-Mail Address already in use")
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
                returnObject = Helper.Response(HttpStatusCode.InternalServerError, "Could not insert user into database")
            }
        }
        return returnObject
    }

    override fun updateUserInRepository(user: UserDTO, id: Int): Helper.Response {
        var returnObject = Helper.Response(HttpStatusCode.OK, "User successfully updated")
        //Use hash in Database, if no new hash has been submitted.
        val userHash: String = getUserHash(user)
        var inputHash: String = user.hash
        if(user.hash == "") {
            inputHash = userHash
        }
        var currentMail: String = String()
        transaction { Users.select { Users.id eq id }.map{
                currentMail = it[Users.mail]
            }
        }

        if (transaction { Users.select { Users.mail eq user.mail }.toList().count() != 0 && user.mail != currentMail})  {
            //Check if E-Mail in use
            returnObject = Helper.Response(HttpStatusCode.Conflict, "E-Mail Address already in use")
        } else {
            try {
                transaction {
                    Users.update({ Users.id eq id }) {
                        it[firstname] = user.firstName
                        it[lastname] = user.lastName
                        it[mail] = user.mail
                        it[hash] = inputHash
                    }
                }
            } catch (e: Exception) {
                println("SqlException: $e")
                returnObject = Helper.Response(HttpStatusCode.InternalServerError, "Could not update user in database")
            }
        }
        return returnObject
    }

    override fun deleteUserInRepository(id: Int): Helper.Response {
        var returnObject = Helper.Response(HttpStatusCode.OK, "User deleted in database")
        try {
            val delete = transaction {
                Users.deleteWhere { Users.id eq id }
            }
            if (delete == 0) {
                returnObject = Helper.Response(HttpStatusCode.Gone, "User not found in database")
            }
        } catch (e: Exception) {
            println("SqlException: $e")
            returnObject = Helper.Response(HttpStatusCode.InternalServerError, "Could not delete user in database")
        }
        return returnObject
    }

    //Helper function to determine the ID of a role.
    override fun getRoleKey(lookupRole: String): Int {
        var foundRole: Int = 0
        roles.forEach(fun(index, role) {
            if (role == lookupRole) foundRole = index
        })
        return foundRole
    }
}