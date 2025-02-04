package me.sersch.http.components.user

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table


//Definition of Table "Users"
object Users : Table() {
    val id: Column<Int> = integer("id").autoIncrement()
    override val primaryKey = PrimaryKey(id, name = "primaryKey")
    val firstname = varchar("firstname", 50)
    val lastname = varchar("lastname", 50)
    val mail = varchar("mail", 50)
    val hash = varchar("hash", 80)
    val role = integer("role")
}