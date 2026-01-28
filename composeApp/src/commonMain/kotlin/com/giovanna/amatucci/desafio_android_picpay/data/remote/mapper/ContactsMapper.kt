package com.giovanna.amatucci.desafio_android_picpay.data.remote.mapper

import com.giovanna.amatucci.desafio_android_picpay.data.local.db.model.ContactUserEntity
import com.giovanna.amatucci.desafio_android_picpay.data.remote.model.UserResponse
import com.giovanna.amatucci.desafio_android_picpay.domain.model.UserInfo

fun UserResponse.toEntity(): ContactUserEntity = ContactUserEntity(
    id = this.id ?: 0,
    name = this.name.orEmpty(),
    img = this.img.orEmpty(),
    username = this.username.orEmpty()
)

fun ContactUserEntity.toDomain(): UserInfo = UserInfo(
    id = this.id, name = this.name, img = this.img, username = this.username
)

fun List<UserResponse>.toEntityList() = map { it.toEntity() }
fun List<ContactUserEntity>.toDomainList() = map { it.toDomain() }