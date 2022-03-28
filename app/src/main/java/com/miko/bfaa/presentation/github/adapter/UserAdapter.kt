package com.miko.bfaa.presentation.github.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.miko.bfaa.base.BaseAdapter
import com.miko.bfaa.base.BaseViewHolder
import com.miko.bfaa.databinding.ItemUserBinding
import com.miko.bfaa.presentation.github.model.User
import com.miko.bfaa.utils.diff.BFAADiff
import com.miko.bfaa.utils.setImageFromUrl

class UserAdapter : BaseAdapter<User, ItemUserBinding, UserAdapter.UserViewHolder>(
    BFAADiff.userDiff
) {
    inner class UserViewHolder(binding: ItemUserBinding) : BaseViewHolder<User, ItemUserBinding>(binding) {
        override fun bind(data: User) {
            with(binding) {
                imgAvatar.setImageFromUrl(data.avatar, 100)
                tvUsername.text = data.username

                root.setOnClickListener {
                    onItemClicked?.invoke(data)
                }
            }
        }
    }

    private var onItemClicked: ((User) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return UserViewHolder(binding)
    }

    fun setOnItemClickedListener(onItemClicked: (User) -> Unit) {
        this.onItemClicked = onItemClicked
    }
}