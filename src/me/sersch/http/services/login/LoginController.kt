package me.sersch.http.services.login

import at.favre.lib.crypto.bcrypt.BCrypt
import me.sersch.http.services.auth.AuthPayload

class LoginController {
    constructor() {

    }

    fun validate(payload: AuthPayload, userCredentials: AuthPayload): Boolean {
        return BCrypt.verifyer().verify(payload.passwordHash.toByteArray(), userCredentials.passwordHash.toByteArray()).verified
    }
}