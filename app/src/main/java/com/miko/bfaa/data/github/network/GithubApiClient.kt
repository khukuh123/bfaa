package com.miko.bfaa.data.github.network

import com.miko.bfaa.data.github.network.response.FollowerListResponse
import com.miko.bfaa.data.github.network.response.FollowingListResponse
import com.miko.bfaa.data.github.network.response.UserDetailResponse
import com.miko.bfaa.data.github.network.response.UserListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubApiClient {
    @GET("users/{username}")
    suspend fun getUserDetail(@Path("username") username: String): Response<UserDetailResponse>

    @GET("users/{username}/followers")
    suspend fun getUserFollowerList(@Path("username") username: String): Response<FollowerListResponse>

    @GET("users/{username}/following")
    suspend fun getUserFollowingList(@Path("username") username: String): Response<FollowingListResponse>

    @GET("search/users")
    suspend fun searchUser(@Query("q") query: String): Response<UserListResponse>
}