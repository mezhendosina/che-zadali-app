package com.che.zadali.sgoapp.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.che.zadali.sgoapp.navigators.LoginNavigator
import com.che.zadali.sgoapp.R
import com.che.zadali.sgoapp.databinding.FragmentContainerBinding
import com.che.zadali.sgoapp.ui.screens.loginActivity.ChooseSchoolFragment
import com.che.zadali.sgoapp.ui.screens.loginActivity.LoginFragment
import com.che.zadali.sgoapp.ui.screens.loginActivity.WelcomeFragment

class LoginActivity : AppCompatActivity(), LoginNavigator {
    private lateinit var binding: FragmentContainerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentContainerBinding.inflate(layoutInflater)
        setContentView(binding.root)


        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .add(R.id.fragmentContainer, WelcomeFragment())
                .commit()
        }
    }

    override fun goBack() {
        onBackPressed()
    }

    override fun chooseSchool() {
        supportFragmentManager.beginTransaction()
            .setReorderingAllowed(true)
            .addToBackStack(null)
            .replace(R.id.fragmentContainer, ChooseSchoolFragment())
            .commit()
    }

    override fun login(schoolId: Int) {
        supportFragmentManager.beginTransaction()
            .setReorderingAllowed(true)
            .addToBackStack(null)
            .replace(R.id.fragmentContainer, LoginFragment.newInstance(schoolId))
            .commit()
    }

}