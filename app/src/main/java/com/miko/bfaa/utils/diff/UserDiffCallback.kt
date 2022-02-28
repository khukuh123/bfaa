package com.miko.bfaa.utils.diff

import androidx.recyclerview.widget.DiffUtil
import com.miko.bfaa.presentation.main.model.User

class UserDiffCallback(
    private val oldUserList: List<User>,
    private val newUserList: List<User>
): DiffUtil.Callback() {

    override fun getOldListSize(): Int =
        oldUserList.size

    override fun getNewListSize(): Int =
        newUserList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldUserList[oldItemPosition].name == newUserList[newItemPosition].name

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldUserList[oldItemPosition] == newUserList[newItemPosition]
}