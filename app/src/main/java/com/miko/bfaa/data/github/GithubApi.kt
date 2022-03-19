package com.miko.bfaa.data.github

import com.miko.bfaa.BuildConfig
import com.miko.bfaa.utils.AppConst
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GithubApi {
    companion object {
        @Volatile
        private var INSTANCE: GithubApiClient? = null

        @JvmStatic
        fun getGithubApi(): GithubApiClient = INSTANCE ?: synchronized(this) {
            val loggingInterceptor =
                HttpLoggingInterceptor().setLevel(
                    if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
                )
            val client = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val original: Request = chain.request()

                    val request = original.newBuilder()
                        .addHeader("Authorization", "token ${AppConst.GITHUB_API_KEY}")
                        .method(original.method, original.body)
                        .build()

                    chain.proceed(request)
                }
                .addInterceptor(loggingInterceptor)
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl(AppConst.GITHUB_API_URL_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            val instance = retrofit.create(GithubApiClient::class.java)
            INSTANCE = instance
            instance
        }
    }
}