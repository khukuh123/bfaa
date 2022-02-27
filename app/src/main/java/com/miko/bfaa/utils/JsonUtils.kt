package com.miko.bfaa.utils

import android.content.res.Resources
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.miko.bfaa.R
import com.miko.bfaa.presentation.main.model.User

fun readJson(resources: Resources): List<User> {
    val gson = Gson()
    resources.openRawResource(R.raw.githubuser).bufferedReader().use {
        return gson.fromJson(it, object : TypeToken<List<User>>() {}.type)
    }
}