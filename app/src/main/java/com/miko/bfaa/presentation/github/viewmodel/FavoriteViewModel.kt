package com.miko.bfaa.presentation.github.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miko.bfaa.data.github.IGithubRepository
import com.miko.bfaa.presentation.github.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class FavoriteViewModel(private val repository: IGithubRepository) : ViewModel() {
    private val _favoriteUsers: MutableLiveData<List<User>> = MutableLiveData()
    private val _isFavoriteUser: MutableLiveData<List<User>> = MutableLiveData()

    val favoriteUsers: LiveData<List<User>>
        get() = _favoriteUsers
    val isFavoriteUser: LiveData<List<User>>
        get() = _isFavoriteUser

    fun getFavoriteUsers() {
        collectData(_favoriteUsers) { repository.getFavoriteUsers() }
    }

    fun setFavoriteUser(user: User) {
        viewModelScope.launch(context = Dispatchers.IO) {
            repository.setFavoriteUser(user)
        }
    }

    fun getFavoriteUserById(id: Int) {
        collectData(_isFavoriteUser) { repository.getFavoriteUserById(id) }
    }

    private fun <T> collectData(liveData: MutableLiveData<T>, source: suspend () -> Flow<T>) {
        viewModelScope.launch {
            source.invoke().collect {
                liveData.postValue(it)
            }
        }
    }
}