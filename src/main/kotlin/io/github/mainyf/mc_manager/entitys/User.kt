package io.github.mainyf.mc_manager.entitys

import io.github.mainyf.common_lib.exts.asUUIDFromByte
import io.github.mainyf.mc_manager.userServiceLazy
import java.util.*

interface IUser {
    val username: String
    val password: String

    fun isEmpty() = username.isBlank() || password.isBlank()

    fun toUser() = userServiceLazy.value.getUser(username)

    fun isExists() = toUser() != null

}

open class User(
    override var username: String = "",
    override var password: String = "",
    var role: MutableList<UserRole> = mutableListOf(UserRole.USER)
) : IUser {

    companion object {
        val EMPTY_UUID = "EMPTY_UUID".asUUIDFromByte()
    }

    var uniqueId: UUID = EMPTY_UUID
        get() {
            if (field == EMPTY_UUID && !username.isBlank() && !password.isBlank()) {
                uniqueId = UUID.randomUUID()
            }
            return field
        }

    fun equalsUserPass(user: User) = this.username == user.username && this.password == user.password

    override fun isEmpty() = uniqueId == EMPTY_UUID

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is User) return false

        if (username != other.username) return false
        if (password != other.password) return false
        if (role != other.role) return false
        if (uniqueId != other.uniqueId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = username.hashCode()
        result = 31 * result + password.hashCode()
        result = 31 * result + role.hashCode()
        result = 31 * result + uniqueId.hashCode()
        return result
    }

}

enum class UserRole {
    USER,
    ADMIN
}