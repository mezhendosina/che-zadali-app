package com.mezhendosina.sgo.app.ui.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.transition.MaterialSharedAxis
import com.mezhendosina.sgo.app.BuildConfig
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.FragmentAboutAppBinding

class AboutAppFragment : Fragment(R.layout.fragment_about_app) {

    private lateinit var binding: FragmentAboutAppBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAboutAppBinding.bind(view)

        binding.appVersion.text = "v" + BuildConfig.VERSION_NAME

        binding.telegramChannelButton.setOnClickListener {
            val telegramIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/che_zadali_app"))

            startActivity(telegramIntent)
        }

        binding.githubRepoButton.setOnClickListener {
            val githubIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://github.com/mezhendosina/che-zadali-app")
            )
            startActivity(githubIntent)
        }
    }
}