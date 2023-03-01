package com.gtech.geoweather.common
import org.mindrot.jbcrypt.BCrypt

object PasswordUtils {
    private const val ROUNDS = 10

    fun hash(password: String): String {
        val salt = BCrypt.gensalt(ROUNDS)
        return BCrypt.hashpw(password, salt)
    }

    fun verify(password: String, hash: String): Boolean {
        return BCrypt.checkpw(password, hash)
    }
}
