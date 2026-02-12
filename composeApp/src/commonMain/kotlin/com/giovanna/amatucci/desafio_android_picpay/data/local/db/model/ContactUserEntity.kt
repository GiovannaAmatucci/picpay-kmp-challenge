package com.giovanna.amatucci.desafio_android_picpay.data.local.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "userEntity")
data class ContactUserEntity(
    @PrimaryKey(false)
    val id: Int, val name: ByteArray,
    val img: String, val username: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        other as ContactUserEntity
        if (id != other.id) return false
        if (!name.contentEquals(other.name)) return false
        if (!username.contentEquals(other.username)) return false
        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + name.contentHashCode()
        result = 31 * result + username.contentHashCode()
        return result
    }
}