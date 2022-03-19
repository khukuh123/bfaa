package com.miko.bfaa.utils.diff

import com.miko.bfaa.base.BFAADiffCallback
import com.miko.bfaa.presentation.github.model.User

class UserDiff(oldUserList: List<User>, newUserList: List<User>) : BFAADiffCallback<User>(oldUserList, newUserList) {
    override fun checkItemsAreSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].id == newList[newItemPosition].id
}