package me.sersch.http.components.role

import org.jetbrains.exposed.sql.Table
import java.util.*

object Roles : Table() {
    val id = uuid("id")
    override val primaryKey = PrimaryKey(id)
    val name = varchar("name", 50)
}