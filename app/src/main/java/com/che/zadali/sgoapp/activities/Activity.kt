package com.che.zadali.sgoapp.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.che.zadali.sgoapp.R
import com.che.zadali.sgoapp.databinding.FragmentContainerBinding
import com.che.zadali.sgoapp.navigators.Navigator
import com.che.zadali.sgoapp.ui.screens.loginActivity.ChooseSchoolFragment
import com.che.zadali.sgoapp.ui.screens.loginActivity.LoginFragment
import com.che.zadali.sgoapp.ui.screens.loginActivity.WelcomeFragment

class Activity : AppCompatActivity(), Navigator {
    private lateinit var binding: FragmentContainerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentContainerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportFragmentManager.beginTransaction()
            .setReorderingAllowed(true)
            .addToBackStack(null)
            .add(R.id.fragmentContainer, WelcomeFragment())
            .commit()

    }

    override fun goBack() {
        onBackPressed()
    }

    override fun chooseSchool(typedSchool: String?) {
        supportFragmentManager.beginTransaction()
            .setReorderingAllowed(true)
            .addToBackStack(null)
            .replace(R.id.fragmentContainer, ChooseSchoolFragment.newInstance(typedSchool))
            .commit()
    }

    override fun login(schoolId: Int, typedSchool: String) {
        supportFragmentManager.beginTransaction()
            .setReorderingAllowed(true)
            .addToBackStack(null)
            .replace(R.id.fragmentContainer, LoginFragment.newInstance(schoolId, typedSchool))
            .commit()
    }

    override fun journal() {
        TODO("Not yet implemented")
    }

    override fun main() {
        TODO("Not yet implemented")
    }

    override fun settings() {
        TODO("Not yet implemented")
    }

    override fun forum() {
        TODO("Not yet implemented")
    }

    override fun messages() {
        TODO("Not yet implemented")
    }

}