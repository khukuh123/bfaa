package com.miko.bfaa.utils

import android.content.res.Resources
import com.miko.bfaa.presentation.main.model.User

object DummyUsers {
    val users: MutableList<User> = mutableListOf()

    fun initDummyUsers(resources: Resources) {
        if (users.isEmpty()) users.addAll(readJson(resources))
    }
}