package com.giovanna.amatucci.desafio_android_picpay.data.remote.mapper

import com.giovanna.amatucci.desafio_android_picpay.data.local.db.model.ContactUserEntity
import com.giovanna.amatucci.desafio_android_picpay.data.remote.model.UserResponse
import com.giovanna.amatucci.desafio_android_picpay.domain.model.UserInfo
import com.giovanna.amatucci.desafio_android_picpay.util.CryptoManager

fun UserResponse.toEntity(crypto: CryptoManager): ContactUserEntity = ContactUserEntity(
    id = this.id ?: 0,
    name = crypto.encrypt(this.name.orEmpty().encodeToByteArray()),
    username = crypto.encrypt(this.username.orEmpty().encodeToByteArray()),
    img = this.img.orEmpty()
)

fun ContactUserEntity.toDomain(crypto: CryptoManager): UserInfo {
    return try {
        UserInfo(
            id = this.id,
            name = crypto.decrypt(this.name).decodeToString(),
            username = crypto.decrypt(this.username).decodeToString(),
            img = this.img
        )
    } catch (e: Exception) {
        UserInfo(this.id, "", "", this.img)
    }
}

fun List<UserResponse>.toEntityList(crypto: CryptoManager) = map { it.toEntity(crypto) }
fun List<ContactUserEntity>.toDomainList(crypto: CryptoManager) = map { it.toDomain(crypto) }