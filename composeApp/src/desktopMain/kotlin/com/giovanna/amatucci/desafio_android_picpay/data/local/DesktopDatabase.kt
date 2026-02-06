package com.giovanna.amatucci.desafio_android_picpay.data.local

import androidx.room.Room
import androidx.room.RoomDatabase
import com.giovanna.amatucci.desafio_android_picpay.data.local.db.AppDatabase
import java.io.File

fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val dbFile = File(System.getProperty("D:\\giovanna\\"), "picpay.db")
    return Room.databaseBuilder<AppDatabase>(
        name = dbFile.absolutePath
    )
}