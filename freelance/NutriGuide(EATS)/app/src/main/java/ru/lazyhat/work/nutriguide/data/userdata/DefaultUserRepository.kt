package ru.lazyhat.work.nutriguide.data.userdata

import androidx.datastore.core.DataStore
import ru.lazyhat.work.nutriguide.data.copy
import ru.lazyhat.work.nutriguide.data.toExternal
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class DefaultUserRepository(
    private val userDataStore: DataStore<LocalUser>,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.Default
) : UserRepository {

    override fun getUserDataStream(): Flow<User> = userDataStore.data.map { it.toExternal() }

    override suspend fun getUserData(): User = userDataStore.data.first().toExternal()

    override fun getCountOfGlassesStream(): Flow<Int> = userDataStore.data.map { it.countGlasses }

    override suspend fun updateUser(user: User) {
        withContext(coroutineDispatcher) {
            userDataStore.updateData {
                it.copy(user)
            }
        }
    }

    override suspend fun updateCountOfGlasses(new: Int) {
        withContext(coroutineDispatcher) {
            userDataStore.updateData { it.copy(countGlasses = new) }
        }
    }
}