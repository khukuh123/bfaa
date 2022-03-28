package com.miko.bfaa.data.github

import android.util.Log
import com.miko.bfaa.data.github.local.GithubDao
import com.miko.bfaa.data.github.network.GithubApiClient
import com.miko.bfaa.data.util.Resource
import com.miko.bfaa.presentation.github.model.User
import com.miko.bfaa.utils.fromUser
import com.miko.bfaa.utils.toUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import org.json.JSONObject
import retrofit2.Response

class GithubRepository private constructor(
    private val api: GithubApiClient,
    private val db: GithubDao,
) : IGithubRepository {

    override suspend fun getUserDetail(username: String): Flow<Resource<User>> =
        api.getUserDetail(username).call { it.toUser() }

    override suspend fun getUserFollowers(username: String): Flow<Resource<List<User>>> =
        api.getUserFollowerList(username).call { it.map { followerItem -> followerItem.toUser() } }

    override suspend fun getUserFollowings(username: String): Flow<Resource<List<User>>> =
        api.getUserFollowingList(username).call { it.map { followingItem -> followingItem.toUser() } }

    override suspend fun searchUser(query: String): Flow<Resource<List<User>>> =
        api.searchUser(query).call { it.userItems.orEmpty().map { users -> users.toUser() } }

    override suspend fun setFavoriteUser(user: User) {
        if (user.isFavorite) db.insertFavoriteUser(fromUser(user)) else db.deleteFavoriteUser(fromUser(user))
    }

    override suspend fun getFavoriteUsers(): Flow<List<User>> =
        db.getFavoriteUser().map {
            Log.d("Count", it.size.toString())
            it.map { user -> user.toUser() }
        }.flowOn(Dispatchers.IO)

    override suspend fun getFavoriteUserById(id: Int): Flow<List<User>> =
        db.getFavoriteUserById(id).map { it.map { user -> user.toUser() } }.flowOn(Dispatchers.IO)

    private suspend fun <T, U> Response<T>.call(map: (T) -> U): Flow<Resource<U>> {
        return flow<Resource<U>> {
            try {
                val response = this@call
                if (response.isSuccessful) {
                    response.body()?.let { value ->
                        emit(Resource.Success(map(value)))
                    }
                } else {
                    with(response) {
                        errorBody()?.string()?.let { value ->
                            val message = JSONObject(value).getString("message")
                            emit(Resource.Error(response.code(), message))
                        }
                    }
                }
            } catch (e: Exception) {
                emit(Resource.Error(999, e.message.orEmpty()))
            }
        }.flowOn(Dispatchers.IO)
    }

    companion object {
        @Volatile
        private var INSTANCE: IGithubRepository? = null

        @JvmStatic
        fun getInstance(api: GithubApiClient, db: GithubDao): IGithubRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: GithubRepository(api, db).also {
                    INSTANCE = it
                }
            }
    }
}