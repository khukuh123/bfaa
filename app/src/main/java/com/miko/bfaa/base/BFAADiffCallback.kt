package com.miko.bfaa.base

import androidx.recyclerview.widget.DiffUtil

abstract class BFAADiffCallback<T>(
    protected val oldList: List<T>,
    protected val newList: List<T>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int =
        oldList.size

    override fun getNewListSize(): Int =
        newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        checkItemsAreSame(oldItemPosition, newItemPosition)

    abstract fun checkItemsAreSame(oldItemPosition: Int, newItemPosition: Int): Boolean

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition] == newList[newItemPosition]
}