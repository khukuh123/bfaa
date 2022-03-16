package com.miko.bfaa.presentation.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.miko.bfaa.databinding.FragmentUserListBinding
import com.miko.bfaa.presentation.main.adapter.UserSimplifiedAdapter
import com.miko.bfaa.utils.BundleKeys
import com.miko.bfaa.utils.DummyUsers

class UserListFragment : Fragment() {

    private val userSimplifiedAdapter: UserSimplifiedAdapter by lazy {
        UserSimplifiedAdapter()
    }

    private var binding: FragmentUserListBinding? = null
    private var isFollower: Boolean = false
    private lateinit var motionLayout: MotionLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentUserListBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initIntent()
        initUI()
        initAction()
        initProcess()
        initObserver()
    }

    private fun initIntent() {
        isFollower = arguments?.getBoolean(BundleKeys.IS_FOLLOWER) ?: false
    }

    private fun initUI() {
        setupRecyclerView()
    }

    private fun initAction() {

    }

    private fun initProcess() {
        if (isFollower) {

        } else {

        }
        userSimplifiedAdapter.setItems(DummyUsers.users)
    }

    private fun initObserver() {

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupRecyclerView() {
        binding?.apply {
            rvUserList.apply {
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                adapter = userSimplifiedAdapter
            }
        }
    }

    companion object {
        fun newInstance(isFollowerList: Boolean, motionLayout: MotionLayout) = UserListFragment().apply {
            this.motionLayout = motionLayout
            arguments = Bundle().apply {
                putBoolean(BundleKeys.IS_FOLLOWER, isFollowerList)
            }
        }
    }
}