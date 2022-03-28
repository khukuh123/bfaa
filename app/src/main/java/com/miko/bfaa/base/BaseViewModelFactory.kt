package com.miko.bfaa.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.miko.bfaa.data.github.IGithubRepository
import com.miko.bfaa.presentation.github.viewmodel.FavoriteViewModel
import com.miko.bfaa.presentation.github.viewmodel.SettingsViewModel
import com.miko.bfaa.presentation.github.viewmodel.UserViewModel
import com.miko.bfaa.utils.SettingPreferences

class BaseViewModelFactory(
    private val repository: IGithubRepository,
    private var settingPreferences: SettingPreferences,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(UserViewModel::class.java) -> {
                UserViewModel(repository) as T
            }
            modelClass.isAssignableFrom(FavoriteViewModel::class.java) -> {
                FavoriteViewModel(repository) as T
            }
            modelClass.isAssignableFrom(SettingsViewModel::class.java) -> {
                SettingsViewModel(settingPreferences) as T
            }
            else -> {
                throw IllegalStateException("Unknown ViewModel class")
            }
        }
    }
}