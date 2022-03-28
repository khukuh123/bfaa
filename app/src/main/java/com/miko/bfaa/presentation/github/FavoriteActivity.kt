package com.miko.bfaa.presentation.github

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.miko.bfaa.R
import com.miko.bfaa.databinding.ActivityFavoriteBinding
import com.miko.bfaa.presentation.github.adapter.FavoriteAdapter
import com.miko.bfaa.presentation.github.viewmodel.FavoriteViewModel
import com.miko.bfaa.utils.ServiceLocator
import com.miko.bfaa.utils.dataStore
import com.miko.bfaa.utils.showContent
import com.miko.bfaa.utils.showEmptyList

class FavoriteActivity : AppCompatActivity() {

    private val favoriteViewModel: FavoriteViewModel by lazy {
        ServiceLocator.getFavoriteViewModel(this, dataStore)
    }
    private val favoriteAdapter: FavoriteAdapter by lazy {
        FavoriteAdapter()
    }

    private var binding: ActivityFavoriteBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        initUI()
        initAction()
        initProcess()
        initObservers()
    }

    private fun initUI() {
        supportActionBar?.apply {
            title = getString(R.string.title_favorite)
            setDisplayHomeAsUpEnabled(true)
        }
        setupRecyclerView()
    }

    private fun initAction() {
        favoriteAdapter.apply {
            setOnFavoriteButtonListener { user ->
                favoriteViewModel.setFavoriteUser(user.apply { isFavorite = false })
            }
            setOnItemClickedListener { username ->
                UserDetailActivity.start(this@FavoriteActivity, username)
            }
        }
    }

    private fun initProcess() {
        favoriteViewModel.getFavoriteUsers()
    }

    private fun initObservers() {
        binding?.apply {
            favoriteViewModel.favoriteUsers.observe(this@FavoriteActivity) {
                if (it.isEmpty()) {
                    msvFavorite.showEmptyList(message = getString(R.string.empty_favorite))
                    favoriteAdapter.submitList(null)
                } else {
                    msvFavorite.showContent()
                    favoriteAdapter.submitList(it)
                }
            }
        }
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

    private fun setupRecyclerView() {
        binding?.apply {
            rvFavorite.apply {
                layoutManager = LinearLayoutManager(this@FavoriteActivity)
                adapter = favoriteAdapter
            }
        }
    }

    companion object {
        @JvmStatic
        fun start(context: Context) {
            context.startActivity(Intent(context, FavoriteActivity::class.java))
        }
    }
}