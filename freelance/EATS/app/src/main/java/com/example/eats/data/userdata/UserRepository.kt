package com.example.eats.data.userdata

import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUserDataStream(): Flow<User>
    suspend fun getUserData(): User
    fun getCountOfGlassesStream(): Flow<Int>
    suspend fun updateUser(user: User)
    suspend fun updateCountOfGlasses(new: Int)
}