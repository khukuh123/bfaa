package com.miko.bfaa.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.bold
import com.miko.bfaa.R
import com.miko.bfaa.databinding.ActivityDetailBinding
import com.miko.bfaa.presentation.main.model.User
import com.miko.bfaa.utils.formatShorter
import com.miko.bfaa.utils.setImageFromString

class DetailUserActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_USER = "user_extra"

        @JvmStatic
        fun start(context: Context, user: User) {
            val starter = Intent(context, DetailUserActivity::class.java)
                .putExtra(EXTRA_USER, user)
            context.startActivity(starter)
        }
    }

    private var binding: ActivityDetailBinding? = null
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        initIntent()
        initUI()
        initProcess()
    }

    private fun initIntent() {
        user = intent?.getParcelableExtra(EXTRA_USER)!!
    }

    private fun initUI() {
        supportActionBar?.apply {
            title = getString(R.string.title_detail_user)
            setDisplayHomeAsUpEnabled(true)
        }
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
            tvName.text = user.name
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
}