package me.sersch.http.components.role

import java.util.*

data class Role (
    override val id: UUID = UUID.randomUUID(),
    override val name: String
    ):RoleInterface