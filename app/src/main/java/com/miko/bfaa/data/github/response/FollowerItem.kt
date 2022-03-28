package com.miko.bfaa.data.github.network.response


import com.google.gson.annotations.SerializedName

data class FollowerItem(
    @SerializedName("avatar_url")
    val avatarUrl: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("login")
    val login: String?
)