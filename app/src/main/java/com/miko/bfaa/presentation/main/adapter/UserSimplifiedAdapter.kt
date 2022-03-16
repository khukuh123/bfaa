package com.miko.bfaa.presentation.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.miko.bfaa.databinding.ItemUserSimplifiedBinding
import com.miko.bfaa.presentation.main.model.User
import com.miko.bfaa.utils.diff.UserDiffCallback
import com.miko.bfaa.utils.setImageFromString

class UserSimplifiedAdapter(
    private val items: MutableList<User> = mutableListOf()
) : RecyclerView.Adapter<UserSimplifiedAdapter.UserViewHolder>() {
    inner class UserViewHolder(private val binding: ItemUserSimplifiedBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: User) {
            with(binding) {
                imgAvatar.setImageFromString(data.avatar)
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

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int =
        items.size

    fun setItems(items: MutableList<User>) {
        val userDiffCallback = UserDiffCallback(this.items, items)
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