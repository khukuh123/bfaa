package com.miko.bfaa.presentation.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.miko.bfaa.databinding.ActivityMainBinding
import com.miko.bfaa.presentation.main.adapter.UserAdapter
import com.miko.bfaa.utils.DummyUsers

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
            DetailUserActivity.start(this, it)
        }
    }

    private fun initUI() {
        setupRecyclerView()
    }

    override fun onDestroy() {
        binding = null
        userAdapter.destroy()
        super.onDestroy()
    }

    private fun setupRecyclerView() {
        binding?.apply {
            rvUser.apply {
                layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
                adapter = userAdapter
                addItemDecoration(DividerItemDecoration(this@MainActivity, DividerItemDecoration.VERTICAL))
            }
        }
    }
}