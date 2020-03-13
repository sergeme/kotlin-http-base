package me.sersch.http.components.user

import org.jetbrains.exposed.sql.Table
import java.util.*

object Users : Table() {
    val id = uuid("id")
    override val primaryKey = PrimaryKey(id)
    val firstname = text("firstname")
    val lastname = text("lastname")
    val mail = text("mail")
    val hash = text("hash")
}