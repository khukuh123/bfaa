package com.miko.bfaa.presentation.github

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.miko.bfaa.R
import com.miko.bfaa.base.BFAAViewModelFactory
import com.miko.bfaa.data.github.GithubApi
import com.miko.bfaa.databinding.ActivityMainBinding
import com.miko.bfaa.presentation.github.adapter.UserAdapter
import com.miko.bfaa.utils.*

class MainActivity : AppCompatActivity() {

    private val userAdapter: UserAdapter by lazy {
        UserAdapter()
    }
    private val githubViewModel: GithubViewModel by lazy {
        ViewModelProvider(this, BFAAViewModelFactory(GithubApi.getGithubApi()))[GithubViewModel::class.java]
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
    }

    private fun initProcess() {
        githubViewModel.searchUser("")
    }

    private fun initObservers() {
        binding?.apply {
            githubViewModel.userList.observe(this@MainActivity,
                onLoading = {
                    msvMain.showLoading()
                },
                onSuccess = { items ->
                    if (items.isEmpty()) {
                        msvMain.showEmptyList(message = getString(R.string.desc_search_result_empty))
                    } else {
                        msvMain.showContent()
                        userAdapter.updateItems(items)
                    }
                },
                onError = { errorMessage ->
                    msvMain.showError(message = errorMessage)
                }
            )
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
                        githubViewModel.searchUser("")
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
                        githubViewModel.searchUser(query.orEmpty())
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean =
                        false
                })
            }
        }
        return super.onCreateOptionsMenu(menu)
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
            }
        }
    }
}