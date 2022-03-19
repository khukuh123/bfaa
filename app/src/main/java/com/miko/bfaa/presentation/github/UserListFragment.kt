package com.miko.bfaa.presentation.github

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.miko.bfaa.R
import com.miko.bfaa.base.BFAAViewModelFactory
import com.miko.bfaa.data.github.GithubApi
import com.miko.bfaa.databinding.FragmentUserListBinding
import com.miko.bfaa.presentation.github.adapter.UserSimplifiedAdapter
import com.miko.bfaa.utils.*

class UserListFragment : Fragment() {

    private val userSimplifiedAdapter: UserSimplifiedAdapter by lazy {
        UserSimplifiedAdapter()
    }
    private val githubViewModel: GithubViewModel by lazy {
        ViewModelProvider(this, BFAAViewModelFactory(GithubApi.getGithubApi()))[GithubViewModel::class.java]
    }

    private var binding: FragmentUserListBinding? = null
    private var isFollower: Boolean = false
    private lateinit var username: String

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
        username = arguments?.getString(BundleKeys.USERNAME).orEmpty()
    }

    private fun initUI() {
        setupRecyclerView()
    }

    private fun initAction() {
        userSimplifiedAdapter.setOnItemClickedListener {
            UserDetailActivity.start(requireContext(), it.username)
        }
    }

    private fun initProcess() {
        if (isFollower) {
            githubViewModel.getUserFollowerList(username)
        } else {
            githubViewModel.getUserFollowingList(username)
        }
    }

    private fun initObserver() {
        binding?.apply {
            if (isFollower) {
                githubViewModel.userFollowers.observe(viewLifecycleOwner,
                    onLoading = {
                        msvUserList.showLoading()
                    },
                    onSuccess = {
                        if (it.isEmpty()) {
                            msvUserList.showEmptyList(message = getString(R.string.desc_follower_empty))
                        } else {
                            msvUserList.showContent()
                            userSimplifiedAdapter.updateItems(it)
                        }
                    },
                    onError = { errorMessage ->
                        msvUserList.showError(message = errorMessage)
                    }
                )
            } else {
                githubViewModel.userFollowings.observe(viewLifecycleOwner,
                    onLoading = {
                        msvUserList.showLoading()
                    },
                    onSuccess = {
                        if (it.isEmpty()) {
                            msvUserList.showEmptyList(message = getString(R.string.desc_following_empty))
                        } else {
                            msvUserList.showContent()
                            userSimplifiedAdapter.updateItems(it)
                        }
                    },
                    onError = { errorMessage ->
                        msvUserList.showError(message = errorMessage)
                    }
                )
            }
        }
    }

    private fun setupRecyclerView() {
        binding?.apply {
            rvUserList.apply {
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                adapter = userSimplifiedAdapter
            }
        }
    }

    companion object {
        fun newInstance(isFollowerList: Boolean, username: String) = UserListFragment().apply {
            arguments = Bundle().apply {
                putBoolean(BundleKeys.IS_FOLLOWER, isFollowerList)
                putString(BundleKeys.USERNAME, username)
            }
        }
    }
}