package me.sersch.http.components.user


import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Table

object Users : IntIdTable() {
    val firstname = text("firstname")
    val lastname = text("lastname")
    val mail = text("mail")
    val hash = text("hash")
}