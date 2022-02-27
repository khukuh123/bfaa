package com.miko.bfaa.presentation.main.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.miko.bfaa.databinding.ItemUserBinding
import com.miko.bfaa.presentation.main.model.User
import com.miko.bfaa.utils.DummyUsers
import com.miko.bfaa.utils.setImageFromString

class UserAdapter(
    private val items: MutableList<User> = DummyUsers.users
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private var binding: ItemUserBinding? = null
    private var onItemClicked: ((User) -> Unit)? = null

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return UserViewHolder(binding!!)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int =
        items.size

    fun destroy() {
        binding = null
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(items: MutableList<User>) {
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun setOnItemClickedListener(onItemClicked: (User) -> Unit) {
        this.onItemClicked = onItemClicked
    }
}