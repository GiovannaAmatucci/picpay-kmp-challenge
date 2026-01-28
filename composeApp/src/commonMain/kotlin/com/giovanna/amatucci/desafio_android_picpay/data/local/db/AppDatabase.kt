package com.giovanna.amatucci.desafio_android_picpay.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.giovanna.amatucci.desafio_android_picpay.data.local.db.dao.ContactUserDao
import com.giovanna.amatucci.desafio_android_picpay.data.local.db.model.ContactUserEntity

@Database(
    entities = [ContactUserEntity::class], version = 1, exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun contactUserDao(): ContactUserDao
}