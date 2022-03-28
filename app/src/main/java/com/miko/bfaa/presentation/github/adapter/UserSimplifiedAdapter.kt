package com.miko.bfaa.presentation.github.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.miko.bfaa.base.BaseAdapter
import com.miko.bfaa.base.BaseViewHolder
import com.miko.bfaa.databinding.ItemUserSimplifiedBinding
import com.miko.bfaa.presentation.github.model.User
import com.miko.bfaa.utils.diff.BFAADiff
import com.miko.bfaa.utils.setImageFromUrl

class UserSimplifiedAdapter : BaseAdapter<User, ItemUserSimplifiedBinding, UserSimplifiedAdapter.UserViewHolder>(
    BFAADiff.userDiff
) {
    inner class UserViewHolder(binding: ItemUserSimplifiedBinding) : BaseViewHolder<User, ItemUserSimplifiedBinding>(binding) {
        override fun bind(data: User) {
            with(binding) {
                imgAvatar.setImageFromUrl(data.avatar, 48)
                tvUsername.text = data.username

                root.setOnClickListener {
                    onItemClicked?.invoke(data)
                }
            }
        }
    }

    private var onItemClicked: ((User) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserSimplifiedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    fun setOnItemClickedListener(onItemClicked: (User) -> Unit) {
        this.onItemClicked = onItemClicked
    }
}