package com.miko.bfaa.data.github

import com.miko.bfaa.data.util.Resource
import com.miko.bfaa.presentation.github.model.User
import kotlinx.coroutines.flow.Flow

interface IGithubRepository {
    suspend fun getUserDetail(username: String): Flow<Resource<User>>

    suspend fun getUserFollowers(username: String): Flow<Resource<List<User>>>

    suspend fun getUserFollowings(username: String): Flow<Resource<List<User>>>

    suspend fun searchUser(query: String): Flow<Resource<List<User>>>

    suspend fun setFavoriteUser(user: User)

    suspend fun getFavoriteUsers(): Flow<List<User>>

    suspend fun getFavoriteUserById(id: Int): Flow<List<User>>
}