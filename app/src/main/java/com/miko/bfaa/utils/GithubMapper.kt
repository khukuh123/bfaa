package com.miko.bfaa.utils

import com.miko.bfaa.data.github.local.entity.FavoriteUserEntity
import com.miko.bfaa.data.github.network.response.FollowerItem
import com.miko.bfaa.data.github.network.response.FollowingItem
import com.miko.bfaa.data.github.network.response.UserDetailResponse
import com.miko.bfaa.data.github.network.response.UserItem
import com.miko.bfaa.presentation.github.model.User

fun UserDetailResponse.toUser() =
    User(
        this.id ?: 0,
        this.avatarUrl ?: "",
        this.company ?: "",
        this.followers ?: 0,
        this.following ?: 0,
        this.location ?: "",
        this.name ?: "",
        this.publicRepos ?: 0,
        this.login ?: "",
        false
    )

fun UserItem.toUser() =
    User(
        this.id ?: 0,
        this.avatarUrl ?: "",
        "",
        0,
        0,
        "",
        "",
        0,
        this.login ?: "",
        false
    )

fun FollowingItem.toUser() =
    User(
        this.id ?: 0,
        this.avatarUrl ?: "",
        "",
        0,
        0,
        "",
        "",
        0,
        this.login ?: "",
        false
    )

fun FollowerItem.toUser() =
    User(
        this.id ?: 0,
        this.avatarUrl ?: "",
        "",
        0,
        0,
        "",
        "",
        0,
        this.login ?: "",
        false
    )

fun FavoriteUserEntity.toUser() =
    User(
        this.id,
        this.avatarUrl,
        "",
        0,
        0,
        this.location,
        this.name,
        0,
        this.username,
        false
    )

fun fromUser(user: User): FavoriteUserEntity =
    FavoriteUserEntity(
        user.id,
        user.avatar,
        user.name,
        user.username,
        user.location
    )