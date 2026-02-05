package com.giovanna.amatucci.desafio_android_picpay.util

import com.giovanna.amatucci.desafio_android_picpay.data.local.db.dao.ContactUserDao
import com.giovanna.amatucci.desafio_android_picpay.data.local.db.model.ContactUserEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update


class FakeContactUserDao : ContactUserDao {
    private val _users = MutableStateFlow<List<ContactUserEntity>>(emptyList())
    var updateCallCount = 0
        private set

    //GIVEN
    fun setInitialData(data: List<ContactUserEntity>) {
        _users.value = data
    }

    override fun getAllUsers(): Flow<List<ContactUserEntity>> = _users

    override suspend fun saveUsers(users: List<ContactUserEntity>) {
        _users.update { currentList ->
            val cleanList = currentList.toMutableList()
            users.forEach { newUser ->
                val index = cleanList.indexOfFirst { it.id == newUser.id }
                if (index != -1) {
                    cleanList[index] = newUser
                } else {
                    cleanList.add(newUser)
                }
            }
            cleanList
        }
    }

    override suspend fun deleteUsersNotIn(idList: List<Int>) {
        _users.update { currentList ->
            currentList.filter { it.id in idList }
        }
    }

    override suspend fun updateContacts(users: List<ContactUserEntity>) {
        updateCallCount++
        saveUsers(users)
        val ids = users.map { it.id }
        deleteUsersNotIn(ids)
    }
}

