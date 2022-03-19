package com.miko.bfaa.presentation.github

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.text.bold
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayoutMediator
import com.miko.bfaa.R
import com.miko.bfaa.base.BFAAPagerAdapter
import com.miko.bfaa.base.BFAAViewModelFactory
import com.miko.bfaa.data.github.GithubApi
import com.miko.bfaa.databinding.ActivityUserDetailBinding
import com.miko.bfaa.presentation.github.model.User
import com.miko.bfaa.utils.*


class UserDetailActivity : AppCompatActivity() {

    private val pagerAdapter: BFAAPagerAdapter by lazy {
        BFAAPagerAdapter(
            this, getTabFragmentList(), mutableListOf(
                getString(R.string.title_follower),
                getString(R.string.title_following)
            )
        )
    }
    private val githubViewModel: GithubViewModel by lazy {
        ViewModelProvider(this, BFAAViewModelFactory(GithubApi.getGithubApi()))[GithubViewModel::class.java]
    }

    private var binding: ActivityUserDetailBinding? = null
    private lateinit var username: String
    private lateinit var name: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        initIntent()
        initUI()
        initProcess()
        initObservers()
    }

    private fun initIntent() {
        username = intent?.getStringExtra(BundleKeys.USERNAME).orEmpty()
    }

    private fun initUI() {
        binding?.apply {
            setSupportActionBar(toolbar)
            toolbar.title = ""
            supportActionBar?.apply {
                title = getString(R.string.title_detail_user)
                setDisplayHomeAsUpEnabled(true)
            }

            motionLayout.setTransitionListener(object : MotionLayout.TransitionListener {
                override fun onTransitionStarted(motionLayout: MotionLayout?, startId: Int, endId: Int) = Unit

                override fun onTransitionChange(motionLayout: MotionLayout?, startId: Int, endId: Int, progress: Float) = Unit

                override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                    when (currentId) {
                        R.id.collapsed -> toolbar.title = name
                        else -> toolbar.title = getString(R.string.title_detail_user)
                    }
                }

                override fun onTransitionTrigger(motionLayout: MotionLayout?, triggerId: Int, positive: Boolean, progress: Float) = Unit

            })
        }
    }

    private fun initProcess() {
        githubViewModel.getUserDetail(username)
    }

    private fun initObservers() {
        binding?.apply {
            githubViewModel.userDetail.observe(this@UserDetailActivity,
                onLoading = {
                    msvUserDetail.showLoading()
                },
                onSuccess = {
                    msvUserDetail.showContent()
                    setupUserDetail(it)
                },
                onError = { errorMessage ->
                    msvUserDetail.showError(message = errorMessage)
                }
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.user_detail_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
            R.id.menu_share -> {
                val profileUrl = "${AppConst.GITHUB_URL_BASE}$username"
                share(getString(R.string.share_text_user).format(profileUrl))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

    private fun setupUserDetail(user: User) {
        binding?.apply {
            imgAvatar.setImageFromUrl(user.avatar)
            tvName.text = user.name.replaceWithDashIfEmpty().also {
                name = it
            }
            tvUsername.text = user.username.replaceWithDashIfEmpty()
            tvLocation.text = user.location.replaceWithDashIfEmpty()
            tvCompany.text = getString(R.string.label_working_at_x).format(user.company.replaceWithDashIfEmpty())
            tvRepositories.text = getString(R.string.label_repositories_x).setDetailUserSpannableText(user.repository.formatShorter())
            tvFollower.text = getString(R.string.label_follower_x).setDetailUserSpannableText(user.follower.formatShorter())
            tvFollowing.text = getString(R.string.label_following_x).setDetailUserSpannableText(user.following.formatShorter())
            setupTabLayout()
        }
    }

    private fun String.setDetailUserSpannableText(value: String): SpannableStringBuilder {
        return SpannableStringBuilder().bold {
            append(this@setDetailUserSpannableText)
        }.append(value)
    }

    private fun setupTabLayout() {
        binding?.apply {
            vpUserDetail.apply {
                adapter = pagerAdapter
            }
            TabLayoutMediator(tabUserDetail, vpUserDetail) { tab, position ->
                tab.text = pagerAdapter.getTitle(position)
            }.attach()
        }
    }

    private fun getTabFragmentList(): MutableList<Fragment> {
        return binding?.let {
            mutableListOf(
                UserListFragment.newInstance(true, username),
                UserListFragment.newInstance(false, username)
            )
        } ?: mutableListOf()
    }


    companion object {
        @JvmStatic
        fun start(context: Context, username: String) {
            val starter = Intent(context, UserDetailActivity::class.java)
                .putExtra(BundleKeys.USERNAME, username)
            context.startActivity(starter)
        }
    }
}