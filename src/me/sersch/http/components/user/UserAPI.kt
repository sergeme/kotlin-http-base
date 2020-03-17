package me.sersch.http.components.user

data class UserAPI(val id: Int,val firstName: String,val lastName: String, val mail: String, val role: Int)
    : UserAbstract(firstName,lastName,mail,role)