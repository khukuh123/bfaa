package com.miko.bfaa.utils

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModelProvider
import com.miko.bfaa.base.BaseViewModelFactory
import com.miko.bfaa.data.github.GithubRepository
import com.miko.bfaa.data.github.IGithubRepository
import com.miko.bfaa.data.github.local.GithubDatabase
import com.miko.bfaa.data.github.network.GithubApi
import com.miko.bfaa.data.github.network.GithubApiClient
import com.miko.bfaa.presentation.github.viewmodel.FavoriteViewModel
import com.miko.bfaa.presentation.github.viewmodel.SettingsViewModel
import com.miko.bfaa.presentation.github.viewmodel.UserViewModel

object ServiceLocator {
    fun getSettingsViewModel(activity: AppCompatActivity, dataStore: DataStore<Preferences>): SettingsViewModel {
        return ViewModelProvider(activity,
            getVieModelFactory(activity.applicationContext, dataStore))[SettingsViewModel::class.java]
    }

    fun getFavoriteViewModel(activity: AppCompatActivity, dataStore: DataStore<Preferences>): FavoriteViewModel {
        return ViewModelProvider(activity,
            getVieModelFactory(activity.applicationContext, dataStore))[FavoriteViewModel::class.java]
    }

    fun getUserViewModel(activity: AppCompatActivity, dataStore: DataStore<Preferences>): UserViewModel {
        return ViewModelProvider(activity,
            getVieModelFactory(activity.applicationContext, dataStore))[UserViewModel::class.java]
    }

    private fun getVieModelFactory(context: Context, dataStore: DataStore<Preferences>): BaseViewModelFactory =
        BaseViewModelFactory(getGithubRepository(context), getDataStore(dataStore))

    private fun getDataStore(dataStore: DataStore<Preferences>): SettingPreferences =
        SettingPreferences.getInstance(dataStore)

    private fun getGithubRepository(context: Context): IGithubRepository =
        GithubRepository.getInstance(
            geGithubApi(),
            getGithubDb(context).githubDao()
        )

    private fun geGithubApi(): GithubApiClient =
        GithubApi.getGithubApi()

    private fun getGithubDb(context: Context): GithubDatabase =
        GithubDatabase.getInstance(context)
}