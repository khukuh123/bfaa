package com.miko.bfaa.presentation.github

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.miko.bfaa.R
import com.miko.bfaa.databinding.ActivitySettingsBinding
import com.miko.bfaa.presentation.github.viewmodel.SettingsViewModel
import com.miko.bfaa.utils.ServiceLocator
import com.miko.bfaa.utils.dataStore

class SettingsActivity : AppCompatActivity() {

    private val settingsViewModel: SettingsViewModel by lazy {
        ServiceLocator.getSettingsViewModel(this, dataStore)
    }

    private var binding: ActivitySettingsBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        initUI()
        initProcess()
        initObserver()
        initAction()
    }

    private fun initUI() {
        supportActionBar?.apply {
            title = getString(R.string.title_settings)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun initProcess() {
        settingsViewModel.getDarkModeTheme()
    }

    private fun initObserver() {
        settingsViewModel.darkModeTheme.observe(this) { isDarkModeActive ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            binding?.scSettingDarkMode?.isChecked = isDarkModeActive
        }
    }

    private fun initAction() {
        binding?.apply {
            scSettingDarkMode.setOnCheckedChangeListener { _, isChecked ->
                settingsViewModel.setDarkModeTheme(isChecked)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

    companion object {
        @JvmStatic
        fun start(context: Context) {
            context.startActivity(Intent(context, SettingsActivity::class.java)).apply {

            }
        }
    }
}