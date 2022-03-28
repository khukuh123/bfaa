package com.miko.bfaa.data.github.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_user")
data class FavoriteUserEntity(
    @PrimaryKey
    val id: Int,
    val avatarUrl: String,
    val name: String,
    val username: String,
    val location: String,
)
