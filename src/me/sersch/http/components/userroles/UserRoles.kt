package me.sersch.http.components.userroles

import me.sersch.http.components.user.Users
import me.sersch.http.components.role.Roles
import org.jetbrains.exposed.sql.Table

object UserRoles : Table() {
    val id = UserRoles.uuid("id")
    override val primaryKey = PrimaryKey(id)
    val user = reference("user", Users.id)
    val role = reference("role", Roles.id)
}