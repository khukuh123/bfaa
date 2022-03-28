package com.miko.bfaa.data.github.network.response


import com.google.gson.annotations.SerializedName

data class UserListResponse(
    @SerializedName("items")
    val userItems: List<UserItem>?
)