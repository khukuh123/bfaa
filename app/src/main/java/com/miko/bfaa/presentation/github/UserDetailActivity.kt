package com.miko.bfaa.presentation.github

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.ContextCompat
import androidx.core.text.bold
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.miko.bfaa.R
import com.miko.bfaa.base.BasePagerAdapter
import com.miko.bfaa.databinding.ActivityUserDetailBinding
import com.miko.bfaa.presentation.github.model.User
import com.miko.bfaa.presentation.github.viewmodel.FavoriteViewModel
import com.miko.bfaa.presentation.github.viewmodel.UserViewModel
import com.miko.bfaa.utils.*


class UserDetailActivity : AppCompatActivity() {

    private val pagerAdapter: BasePagerAdapter by lazy {
        BasePagerAdapter(
            this, getTabFragmentList(), mutableListOf(
                getString(R.string.title_follower),
                getString(R.string.title_following)
            )
        )
    }
    private val userViewModel: UserViewModel by lazy {
        ServiceLocator.getUserViewModel(this, dataStore)
    }
    private val favoriteViewModel: FavoriteViewModel by lazy {
        ServiceLocator.getFavoriteViewModel(this, dataStore)
    }

    private var binding: ActivityUserDetailBinding? = null
    private lateinit var username: String
    private var user: User? = null
    private var favoriteMenu: MenuItem? = null

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
                        R.id.collapsed -> toolbar.title = user?.name.orEmpty()
                        else -> toolbar.title = getString(R.string.title_detail_user)
                    }
                }

                override fun onTransitionTrigger(motionLayout: MotionLayout?, triggerId: Int, positive: Boolean, progress: Float) = Unit

            })
        }
    }

    private fun initProcess() {
        userViewModel.getUserDetail(username)
    }

    private fun initObservers() {
        binding?.apply {
            favoriteViewModel.isFavoriteUser.observe(this@UserDetailActivity) {
                favoriteMenu?.apply {
                    isChecked = it.isNotEmpty()
                    setFavoriteMenuState()
                }
                user?.isFavorite = it.isNotEmpty()
            }
            userViewModel.userDetail.observe(this@UserDetailActivity,
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
        favoriteMenu = menu?.findItem(R.id.menu_favorite)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
            R.id.menu_favorite -> {
                user?.let {
                    it.isFavorite = !it.isFavorite
                    item.isChecked = !it.isFavorite
                    item.setFavoriteMenuState()
                    favoriteViewModel.setFavoriteUser(it)
                }
            }
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

    private fun MenuItem.setFavoriteMenuState() {
        this.icon = ContextCompat.getDrawable(
            this@UserDetailActivity,
            if (this.isChecked) R.drawable.ic_baseline_favorite_24 else R.drawable.ic_baseline_favorite_border_24)

    }

    private fun setupUserDetail(user: User) {
        favoriteViewModel.getFavoriteUserById(user.id)
        this.user = user
        binding?.apply {
            imgAvatar.setImageFromUrl(user.avatar)
            tvName.text = user.name.replaceWithDashIfEmpty()
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