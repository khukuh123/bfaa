package com.miko.bfaa.data.github.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.miko.bfaa.data.github.local.entity.FavoriteUserEntity

@Database(entities = [FavoriteUserEntity::class], version = 1)
abstract class GithubDatabase : RoomDatabase() {
    abstract fun githubDao(): GithubDao

    companion object {
        @Volatile
        private var INSTANCE: GithubDatabase? = null

        @JvmStatic
        fun getInstance(context: Context): GithubDatabase = INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room
                .databaseBuilder(
                    context.applicationContext,
                    GithubDatabase::class.java,
                    "github")
                .build()
        }
    }
}