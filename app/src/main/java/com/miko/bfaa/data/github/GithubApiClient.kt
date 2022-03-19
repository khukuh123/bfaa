package com.miko.bfaa.data.github

import com.miko.bfaa.data.github.response.FollowerListResponse
import com.miko.bfaa.data.github.response.FollowingListResponse
import com.miko.bfaa.data.github.response.UserDetailResponse
import com.miko.bfaa.data.github.response.UserListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubApiClient {
    @GET("users/{username}")
    fun getUserDetail(@Path("username") username: String): Call<UserDetailResponse>

    @GET("users/{username}/followers")
    fun getUserFollowerList(@Path("username") username: String): Call<FollowerListResponse>

    @GET("users/{username}/following")
    fun getUserFollowingList(@Path("username") username: String): Call<FollowingListResponse>

    @GET("search/users")
    fun searchUser(@Query("q") query: String): Call<UserListResponse>
}