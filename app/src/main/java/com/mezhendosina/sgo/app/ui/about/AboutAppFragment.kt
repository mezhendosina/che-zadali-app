package com.mezhendosina.sgo.app.ui.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.transition.platform.MaterialSharedAxis
import com.mezhendosina.sgo.app.BuildConfig
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.FragmentAboutAppBinding
import com.mezhendosina.sgo.data.toMD5

class AboutAppFragment : Fragment(R.layout.fragment_about_app) {

    private lateinit var binding: FragmentAboutAppBinding

    private val viewModel: AboutAppViewModel by viewModels()

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

        val editText = EditText(requireContext())
        binding.specialThanks.setOnLongClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setView(editText)
                .setPositiveButton("Ок") { dialog, _ ->
                    if (editText.text.toString().toMD5() == "b72e9e251683b5b92c4fbc82d5d354d5") {
                        viewModel.registerUser()
                        dialog.dismiss()
                        editText.invalidate()
                    }
                }
                .show()
            true
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