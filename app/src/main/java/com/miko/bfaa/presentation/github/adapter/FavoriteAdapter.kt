package com.miko.bfaa.presentation.github.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.miko.bfaa.base.BaseAdapter
import com.miko.bfaa.base.BaseViewHolder
import com.miko.bfaa.databinding.ItemFavoriteBinding
import com.miko.bfaa.presentation.github.model.User
import com.miko.bfaa.utils.diff.BFAADiff
import com.miko.bfaa.utils.replaceWithDashIfEmpty
import com.miko.bfaa.utils.setImageFromUrl

class FavoriteAdapter : BaseAdapter<User, ItemFavoriteBinding, FavoriteAdapter.FavoriteViewHolder>(
    BFAADiff.userDiff
) {
    inner class FavoriteViewHolder(binding: ItemFavoriteBinding) : BaseViewHolder<User, ItemFavoriteBinding>(binding) {
        override fun bind(data: User) {
            with(binding) {
                imgAvatar.setImageFromUrl(data.avatar, 48)
                tvName.text = data.name
                tvUsername.text = data.username
                tvLocation.text = data.location.replaceWithDashIfEmpty()

                root.setOnClickListener {
                    onItemClicked?.invoke(data.username)
                }
                btnFavorite.setOnClickListener {
                    onFavoriteButtonClicked?.invoke(data)
                }
            }
        }
    }

    private var onFavoriteButtonClicked: ((User) -> Unit)? = null
    private var onItemClicked: ((String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return FavoriteViewHolder(binding)
    }

    fun setOnFavoriteButtonListener(listener: (user: User) -> Unit) {
        onFavoriteButtonClicked = listener
    }

    fun setOnItemClickedListener(listener: (username: String) -> Unit) {
        onItemClicked = listener
    }
}