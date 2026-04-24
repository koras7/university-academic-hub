package com.example.academichub.util

import java.security.MessageDigest

object PasswordEncryption {

    fun hashPassword(password: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(password.toByteArray(Charsets.UTF_8))
        return hashBytes.joinToString("") { "%02x".format(it) }
    }

    fun verifyPassword(password: String, hashedPassword: String): Boolean {
        return hashPassword(password) == hashedPassword
    }
}
