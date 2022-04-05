package com.che.zadali.sgoapp.activities.modalDrawerActivities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.che.zadali.sgoapp.R
import com.che.zadali.sgoapp.databinding.NoMainContainerBinding
import com.che.zadali.sgoapp.navigators.Navigator
import com.che.zadali.sgoapp.ui.screens.mainActivity.ChangeControlQuestionFragment
import com.che.zadali.sgoapp.ui.screens.mainActivity.ChangePasswordFragment
import com.che.zadali.sgoapp.ui.screens.mainActivity.SettingsFragment

class SettingsActivity : AppCompatActivity(), Navigator {
    private lateinit var binding: NoMainContainerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = NoMainContainerBinding.inflate(layoutInflater)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setTitle(R.string.settings)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction()
            .setReorderingAllowed(true)
            .replace(R.id.no_main_container_fragment, SettingsFragment(supportActionBar))
            .commit()
        supportActionBar?.setDisplayShowCustomEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

    }

    override fun goBack() {
        onBackPressed()
    }

    override fun settings() {
        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .setReorderingAllowed(true)
            .replace(R.id.no_main_container_fragment, SettingsFragment(supportActionBar))
            .commit()
    }

    override fun changePassword() {
        binding.toolbar.setTitle(R.string.change_password)
        supportFragmentManager.beginTransaction()
            .setReorderingAllowed(true)
            .addToBackStack(null)
            .replace(R.id.no_main_container_fragment, ChangePasswordFragment())
            .commit()
    }

    override fun changeControlQuestion() {
        binding.toolbar.setTitle(R.string.change_control_question)
        supportFragmentManager.beginTransaction()
            .setReorderingAllowed(true)
            .addToBackStack(null)
            .replace(R.id.no_main_container_fragment, ChangeControlQuestionFragment())
            .commit()
    }

    override fun chooseSchool(typedSchool: String?) {}

    override fun login(schoolId: Int, typedSchool: String) {}

    override fun messageItem(messageId: Int) {}
    override fun messages() {
        TODO("Not yet implemented")
    }
}