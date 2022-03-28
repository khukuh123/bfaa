package com.miko.bfaa.presentation.github.viewmodel

import androidx.lifecycle.*
import com.miko.bfaa.data.github.IGithubRepository
import com.miko.bfaa.data.util.Resource
import com.miko.bfaa.presentation.github.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class UserViewModel(private val repository: IGithubRepository) : ViewModel() {

    private val _userDetail: MutableLiveData<Resource<User>> = MutableLiveData()
    private val _userFollowers: MutableLiveData<Resource<List<User>>> = MutableLiveData()
    private val _userFollowings: MediatorLiveData<Resource<List<User>>> = MediatorLiveData()
    private val _userList: MutableLiveData<Resource<List<User>>> = MutableLiveData()

    val userDetail: LiveData<Resource<User>>
        get() = _userDetail
    val userFollowers: LiveData<Resource<List<User>>>
        get() = _userFollowers
    val userFollowings: LiveData<Resource<List<User>>>
        get() = _userFollowings
    val userList: LiveData<Resource<List<User>>>
        get() = _userList


    init {
        _userDetail.value = Resource.Loading()
        _userFollowers.value = Resource.Loading()
        _userFollowings.value = Resource.Loading()
        _userList.value = Resource.Loading()
    }

    fun getUserDetail(username: String) {
        collectData(_userDetail) {
            repository.getUserDetail(username)
        }
    }

    fun searchUser(username: String) {
        val modifiedUsername = username.ifEmpty { "\"\"" }
        collectData(_userList) {
            repository.searchUser(modifiedUsername)
        }
    }

    fun getUserFollowerList(username: String) {
        collectData(_userFollowers) {
            repository.getUserFollowers(username)
        }
    }

    fun getUserFollowingList(username: String) {
        collectData(_userFollowings) {
            repository.getUserFollowings(username)
        }
    }

    private fun <T> collectData(liveData: MutableLiveData<Resource<T>>, source: suspend () -> Flow<Resource<T>>) {
        liveData.value = Resource.Loading()
        viewModelScope.launch {
            source.invoke().collect {
                liveData.postValue(it)
            }
        }
    }
}