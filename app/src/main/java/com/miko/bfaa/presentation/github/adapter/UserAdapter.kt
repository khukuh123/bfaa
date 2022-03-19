package com.miko.bfaa.presentation.github.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.miko.bfaa.databinding.ItemUserBinding
import com.miko.bfaa.presentation.github.model.User
import com.miko.bfaa.utils.diff.UserDiff
import com.miko.bfaa.utils.setImageFromUrl

class UserAdapter(
    private val items: MutableList<User> = mutableListOf()
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    inner class UserViewHolder(private val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: User) {
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

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int =
        items.size

    fun updateItems(items: List<User>) {
        val userDiffCallback = UserDiff(this.items, items)
        val diffResult = DiffUtil.calculateDiff(userDiffCallback)

        this.items.apply {
            clear()
            addAll(items)
        }
        diffResult.dispatchUpdatesTo(this)
    }

    fun setOnItemClickedListener(onItemClicked: (User) -> Unit) {
        this.onItemClicked = onItemClicked
    }
}