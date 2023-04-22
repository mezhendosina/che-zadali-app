/*
 * Copyright 2023 Eugene Menshenin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mezhendosina.sgo.app.ui.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.transition.platform.MaterialSharedAxis
import com.mezhendosina.sgo.app.BuildConfig
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.FragmentAboutAppBinding
import io.noties.markwon.Markwon
import io.noties.markwon.html.HtmlPlugin

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

        val appVersion = requireContext().getString(R.string.app_version, BuildConfig.VERSION_NAME)
        binding.appVersion.text = appVersion

        val markwon = Markwon.builder(requireContext()).usePlugin(HtmlPlugin.create()).build()
        markwon.setMarkdown(
            binding.specialThanks,
            "Даниилу Барменкову<br>Вячеславу Сумину<br>Марии Левчановой<br><a href=\"https://github.com/ArtemBay\">ArtemBay</a>"
        )

        binding.telegramChannelButton.setOnClickListener {
            val telegramIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/sgoapp"))

            startActivity(telegramIntent)
        }

        binding.githubRepoButton.setOnClickListener {
            val githubIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://github.com/mezhendosina/sgo-app")
            )
            startActivity(githubIntent)
        }

        binding.appWebSiteButton.setOnClickListener {
            val githubIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://sgoapp.ru")
            )
            startActivity(githubIntent)
        }
    }
}