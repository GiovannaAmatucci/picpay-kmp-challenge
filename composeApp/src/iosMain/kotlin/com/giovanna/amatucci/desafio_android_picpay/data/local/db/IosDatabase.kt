package com.giovanna.amatucci.desafio_android_picpay.data.local.db

import androidx.room.Room
import androidx.room.RoomDatabase
import platform.Foundation.NSHomeDirectory
import com.giovanna.amatucci.desafio_android_picpay.data.local.db.AppDatabase_Impl

fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val dbFilePath = NSHomeDirectory() + "/Documents/picpay-db"
    return Room.databaseBuilder<AppDatabase>(
        name =  dbFilePath,
        factory = { AppDatabase_Impl() }
    )
}