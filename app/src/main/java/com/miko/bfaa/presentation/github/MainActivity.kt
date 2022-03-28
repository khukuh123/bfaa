package com.miko.bfaa.presentation.github

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.miko.bfaa.R
import com.miko.bfaa.databinding.ActivityMainBinding
import com.miko.bfaa.presentation.github.adapter.UserAdapter
import com.miko.bfaa.presentation.github.viewmodel.SettingsViewModel
import com.miko.bfaa.presentation.github.viewmodel.UserViewModel
import com.miko.bfaa.utils.*

class MainActivity : AppCompatActivity() {

    private val userAdapter: UserAdapter by lazy {
        UserAdapter()
    }
    private val userViewModel: UserViewModel by lazy {
        ServiceLocator.getUserViewModel(this, dataStore)
    }
    private val settingsViewModel: SettingsViewModel by lazy {
        ServiceLocator.getSettingsViewModel(this, dataStore)
    }

    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        initUI()
        initAction()
        initProcess()
        initObservers()
    }

    private fun initUI() {
        setupRecyclerView()
        binding?.apply {
        }
    }

    private fun initAction() {
        userAdapter.setOnItemClickedListener {
            UserDetailActivity.start(this, it.username)
        }
        binding?.fabFavorite?.setOnClickListener {
            FavoriteActivity.start(this)
        }
    }

    private fun initProcess() {
        userViewModel.searchUser("")
        settingsViewModel.getDarkModeTheme()
    }

    private fun initObservers() {
        binding?.apply {
            userViewModel.userList.observe(this@MainActivity,
                onLoading = {
                    msvMain.showLoading()
                },
                onSuccess = { items ->
                    if (items.isEmpty()) {
                        msvMain.showEmptyList(message = getString(R.string.desc_search_result_empty))
                    } else {
                        msvMain.showContent()
                        userAdapter.submitList(items)
                    }
                },
                onError = { errorMessage ->
                    msvMain.showError(message = errorMessage)
                }
            )
            settingsViewModel.darkModeTheme.observe(this@MainActivity) { isDarkModeActive ->
                if (isDarkModeActive) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        binding?.apply {
            val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
            val searchView = menu?.findItem(R.id.menu_search)?.also {
                it.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
                    override fun onMenuItemActionExpand(item: MenuItem?): Boolean =
                        true

                    override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                        userViewModel.searchUser("")
                        return true
                    }
                })
            }?.actionView as SearchView

            searchView.apply {
                maxWidth = Int.MAX_VALUE
                setSearchableInfo(searchManager.getSearchableInfo(componentName))
                queryHint = getString(R.string.hint_user_search)
                setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        clearFocus()
                        userViewModel.searchUser(query.orEmpty())
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean =
                        false
                })
            }
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_settings -> SettingsActivity.start(this)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

    private fun setupRecyclerView() {
        binding?.apply {
            rvUser.apply {
                layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
                adapter = userAdapter
                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                        if (newState == RecyclerView.SCROLL_STATE_IDLE) fabFavorite.show() else fabFavorite.hide()
                        super.onScrollStateChanged(recyclerView, newState)
                    }
                })
            }
        }
    }
}