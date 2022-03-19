package com.miko.bfaa.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.miko.bfaa.data.github.GithubApiClient
import com.miko.bfaa.presentation.github.GithubViewModel

class BFAAViewModelFactory(private val api: GithubApiClient) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GithubViewModel::class.java)) {
            return GithubViewModel(api) as T
        } else {
            throw IllegalStateException("Unknown ViewModel class")
        }
    }
}