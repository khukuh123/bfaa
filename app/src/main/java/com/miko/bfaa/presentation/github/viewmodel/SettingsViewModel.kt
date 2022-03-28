package com.miko.bfaa.presentation.github.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miko.bfaa.utils.SettingPreferences
import kotlinx.coroutines.launch

class SettingsViewModel(private val settings: SettingPreferences) : ViewModel() {
    private val _darkModeTheme: MutableLiveData<Boolean> = MutableLiveData()

    val darkModeTheme: LiveData<Boolean>
        get() = _darkModeTheme

    fun setDarkModeTheme(isDarkMode: Boolean) {
        viewModelScope.launch {
            settings.setDarkModeSetting(isDarkMode)
        }
    }

    fun getDarkModeTheme() {
        viewModelScope.launch {
            settings.getDarkModeSetting().collect {
                _darkModeTheme.value = it
            }
        }
    }
}