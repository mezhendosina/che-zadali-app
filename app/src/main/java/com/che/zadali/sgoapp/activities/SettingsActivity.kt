package com.che.zadali.sgoapp.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.che.zadali.sgoapp.R
import com.che.zadali.sgoapp.databinding.NoMainContainerBinding
import com.che.zadali.sgoapp.ui.screens.mainActivity.SettingsFragment

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: NoMainContainerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = NoMainContainerBinding.inflate(layoutInflater)

        setContentView(binding.root)
        supportFragmentManager.beginTransaction()
            .setReorderingAllowed(true)
            .replace(R.id.no_main_container_fragment, SettingsFragment())
            .commit()
        binding.noMainToolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}