package me.sersch.http.components.user

//Interface for User Class
interface UserInterface {
    val id: Int
    val firstName: String
    val lastName: String
    val mail: String
    val role: Int
}