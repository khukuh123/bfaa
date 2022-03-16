package com.miko.bfaa.base

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class BFAAPagerAdapter(
    activity: FragmentActivity,
    private val items: List<Fragment> = listOf(),
    private val titles: List<String> = listOf()
) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int =
        items.size

    override fun createFragment(position: Int): Fragment =
        items[position]

    fun getTitle(position: Int) = titles[position]
}