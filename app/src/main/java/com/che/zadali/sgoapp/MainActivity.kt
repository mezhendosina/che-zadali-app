package com.che.zadali.sgoapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainer
import com.che.zadali.sgoapp.databinding.FragmentContainerBinding

import com.che.zadali.sgoapp.screens.mainActivity.MainFragment

class MainActivity : AppCompatActivity(), MainNavigator {
    private lateinit var binding: FragmentContainerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentContainerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null){
            supportFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .add(R.id.fragmentContainer, MainFragment())
                .commit()
        }
    }

    override fun goBack() {
        TODO("Not yet implemented")
    }

    override fun journal() {
        TODO("Not yet implemented")
    }

    override fun main() {
        supportFragmentManager.beginTransaction()
            .setReorderingAllowed(true)
            .addToBackStack(null)
            .replace(R.id.fragmentContainer, MainFragment())
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