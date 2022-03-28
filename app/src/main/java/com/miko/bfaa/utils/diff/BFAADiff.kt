package com.miko.bfaa.utils.diff

import androidx.recyclerview.widget.DiffUtil
import com.miko.bfaa.presentation.github.model.User

object BFAADiff {
    val userDiff = object : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean =
            oldItem == newItem
    }
}