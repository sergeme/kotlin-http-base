package me.sersch.http.components.role

import me.sersch.http.components.user.User
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*
import kotlin.collections.ArrayList

class RoleController {
    constructor() {

    }

    fun hasRole(user: User, role: String): Boolean {
        return (roles[user.role] == role)
    }

    fun getKey(lookupRole: String): Int {
        var foundRole:Int = 0
        roles.forEach(fun (index, role) {
            if(role == lookupRole) foundRole = index
        })
        return foundRole
    }
}