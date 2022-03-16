package com.miko.bfaa.presentation.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.text.bold
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.miko.bfaa.R
import com.miko.bfaa.base.BFAAPagerAdapter
import com.miko.bfaa.databinding.ActivityUserDetailBinding
import com.miko.bfaa.presentation.main.model.User
import com.miko.bfaa.utils.*
import com.miko.bfaa.utils.BundleKeys.USER


class UserDetailActivity : AppCompatActivity() {

    private val pagerAdapter: BFAAPagerAdapter by lazy {
        BFAAPagerAdapter(
            this, getTabFragmentList(), mutableListOf(
                getString(R.string.title_follower),
                getString(R.string.title_following)
            )
        )
    }

    private var binding: ActivityUserDetailBinding? = null
    private lateinit var user: User
    private lateinit var name: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        initIntent()
        initUI()
        initProcess()
    }

    private fun initIntent() {
        user = intent?.getParcelableExtra(USER)!!
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

        setupTabLayout()
    }

    private fun initProcess() {
        setupUserDetail(user)
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupUserDetail(user: User) {
        binding?.apply {
            imgAvatar.setImageFromString(user.avatar)
            tvName.text = user.name.also {
                name = it
            }
            tvUsername.text = user.username
            tvLocation.text = user.location
            tvCompany.text = getString(R.string.label_working_at_x).format(user.company)
            tvRepositories.text = getString(R.string.label_repositories_x).setDetailUserSpannableText(user.repository.formatShorter())
            tvFollower.text = getString(R.string.label_follower_x).setDetailUserSpannableText(user.follower.formatShorter())
            tvFollowing.text = getString(R.string.label_following_x).setDetailUserSpannableText(user.following.formatShorter())
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
                UserListFragment.newInstance(true, it.motionLayout),
                UserListFragment.newInstance(false, it.motionLayout)
            )
        } ?: mutableListOf()
    }


    companion object {
        @JvmStatic
        fun start(context: Context, user: User) {
            val starter = Intent(context, UserDetailActivity::class.java)
                .putExtra(USER, user)
            context.startActivity(starter)
        }
    }
}