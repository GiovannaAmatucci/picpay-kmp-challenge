package com.giovanna.amatucci.desafio_android_picpay.data.local

import androidx.room.Room
import androidx.room.RoomDatabase
import com.giovanna.amatucci.desafio_android_picpay.data.local.db.AppDatabase
import java.io.File
fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val dbFolder = File("D:\\giovanna\\")
    if (!dbFolder.exists()) dbFolder.mkdirs()
    val dbFile = File(dbFolder, "picpay_kmp.db")

    return Room.databaseBuilder<AppDatabase>(
        name = dbFile.absolutePath
    )
}