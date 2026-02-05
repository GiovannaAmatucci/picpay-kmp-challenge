package com.giovanna.amatucci.desafio_android_picpay.data.local.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.giovanna.amatucci.desafio_android_picpay.data.local.db.model.ContactUserEntity
import kotlinx.coroutines.flow.Flow
@Dao
interface ContactUserDao {
    @Query("SELECT * FROM userEntity")
    fun getAllUsers(): Flow<List<ContactUserEntity>>
    @Upsert
    suspend fun saveUsers(users: List<ContactUserEntity>)
    @Query("DELETE FROM userEntity WHERE id NOT IN (:idList)")
    suspend fun deleteUsersNotIn(idList: List<Int>)
    @Transaction
    suspend fun updateContacts(users: List<ContactUserEntity>) {
        saveUsers(users)
        val ids = users.map { it.id }
        deleteUsersNotIn(ids)
    }
}