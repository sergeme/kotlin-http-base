package me.sersch.http.components.role

import org.jetbrains.exposed.sql.Table
import java.util.*

//Roles default and admin must exist for userController to work
val roles = mapOf(1 to "default", 2 to "admin")