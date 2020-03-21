package me.sersch.http.services.auth
//Class for receiving authentication requests.
data class AuthPayload(val userName: String, val password: String)