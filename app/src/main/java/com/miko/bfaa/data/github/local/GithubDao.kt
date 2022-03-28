package com.miko.bfaa.data.github.local

import androidx.room.*
import com.miko.bfaa.data.github.local.entity.FavoriteUserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GithubDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteUser(favoriteUserEntity: FavoriteUserEntity)

    @Delete
    suspend fun deleteFavoriteUser(favoriteUserEntity: FavoriteUserEntity)

    @Query("SELECT * FROM favorite_user")
    fun getFavoriteUser(): Flow<List<FavoriteUserEntity>>

    @Query("SELECT * FROM favorite_user WHERE id = :id")
    fun getFavoriteUserById(id: Int): Flow<List<FavoriteUserEntity>>
}