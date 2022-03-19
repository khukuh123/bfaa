package com.miko.bfaa.presentation.github

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.miko.bfaa.data.github.GithubApiClient
import com.miko.bfaa.data.util.Resource
import com.miko.bfaa.presentation.github.model.User
import com.miko.bfaa.utils.toUser
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GithubViewModel(private val api: GithubApiClient) : ViewModel() {

    private val _userDetail: MutableLiveData<Resource<User>> = MutableLiveData()
    private val _userFollowers: MutableLiveData<Resource<List<User>>> = MutableLiveData()
    private val _userFollowings: MutableLiveData<Resource<List<User>>> = MutableLiveData()
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
        api.getUserDetail(username).enqueue(_userDetail) {
            it.toUser()
        }
    }

    fun searchUser(username: String) {
        val modifiedUsername = username.ifEmpty { "\"\"" }
        api.searchUser(modifiedUsername).enqueue(_userList) {
            it.userItems?.map { userItem ->
                userItem.toUser()
            }.orEmpty()
        }
    }

    fun getUserFollowerList(username: String) {
        api.getUserFollowerList(username).enqueue(_userFollowers) {
            it.map { followerItem ->
                followerItem.toUser()
            }
        }
    }

    fun getUserFollowingList(username: String) {
        api.getUserFollowingList(username).enqueue(_userFollowings) {
            it.map { followingItem ->
                followingItem.toUser()
            }
        }
    }

    private fun <T, R> Call<T>.enqueue(liveData: MutableLiveData<Resource<R>>, map: (T) -> R) {
        liveData.value = Resource.Loading()
        this.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        liveData.postValue(Resource.Success(map(it)))
                    }
                } else {
                    with(response) {
                        response.errorBody()?.string()?.let {
                            val message = JSONObject(it).getString("message")
                            liveData.postValue(Resource.Error(code(), message))
                        }
                    }
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                liveData.postValue(Resource.Error(999, t.message.orEmpty()))
            }
        })
    }
}