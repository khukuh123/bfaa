package com.miko.bfaa.utils

import com.miko.bfaa.data.github.response.FollowerItem
import com.miko.bfaa.data.github.response.FollowingItem
import com.miko.bfaa.data.github.response.UserDetailResponse
import com.miko.bfaa.data.github.response.UserItem
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
        this.login ?: ""
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
        this.login ?: ""
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
        this.login ?: ""
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
        this.login ?: ""
    )