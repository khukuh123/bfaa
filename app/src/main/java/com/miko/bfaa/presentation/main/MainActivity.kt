package com.miko.bfaa.presentation.main

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.LinearLayoutManager
import com.miko.bfaa.R
import com.miko.bfaa.databinding.ActivityMainBinding
import com.miko.bfaa.presentation.main.adapter.UserAdapter
import com.miko.bfaa.utils.DummyUsers
import com.miko.bfaa.utils.showToast

class MainActivity : AppCompatActivity() {

    private val userAdapter: UserAdapter by lazy {
        UserAdapter()
    }

    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        DummyUsers.initDummyUsers(resources)

        initUI()
        initAction()
        initProcess()
    }

    private fun initProcess() {
        userAdapter.setItems(DummyUsers.users)
    }

    private fun initAction() {
        userAdapter.setOnItemClickedListener {
            UserDetailActivity.start(this, it)
        }
    }

    private fun initUI() {
        setupRecyclerView()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        binding?.apply {
            val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
            val searchView = menu?.findItem(R.id.menu_search)?.actionView as SearchView

            searchView.apply {
                maxWidth = Int.MAX_VALUE
                setSearchableInfo(searchManager.getSearchableInfo(componentName))
                queryHint = getString(R.string.hint_user_search)
                setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        clearFocus()
                        showToast(query.orEmpty())
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