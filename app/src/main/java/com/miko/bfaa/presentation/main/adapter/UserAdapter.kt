package com.miko.bfaa.presentation.main.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.miko.bfaa.databinding.ItemUserBinding
import com.miko.bfaa.presentation.main.model.User
import com.miko.bfaa.utils.DummyUsers
import com.miko.bfaa.utils.diff.UserDiffCallback
import com.miko.bfaa.utils.setImageFromString

class UserAdapter(
    private val items: MutableList<User> = mutableListOf()
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    inner class UserViewHolder(private val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: User) {
            with(binding) {
                imgAvatar.setImageFromString(data.avatar)
                tvName.text = data.name
                tvUsername.text = data.username
                tvLocation.text = data.location

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