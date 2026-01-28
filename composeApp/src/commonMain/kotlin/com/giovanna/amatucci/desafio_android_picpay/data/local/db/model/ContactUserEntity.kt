package com.giovanna.amatucci.desafio_android_picpay.data.local.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "userEntity")
data class ContactUserEntity(
    @PrimaryKey(false)
    val id: Int,
    val name: String,
    val img: String,
    val username: String
)