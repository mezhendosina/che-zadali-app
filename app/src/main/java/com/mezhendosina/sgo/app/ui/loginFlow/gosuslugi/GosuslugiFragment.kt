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

package com.mezhendosina.sgo.app.ui.loginFlow.gosuslugi

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.FragmentGosuslugiBinding
import com.mezhendosina.sgo.app.ui.loginFlow.gosuslugiResult.GosuslugiResultFragment
import com.mezhendosina.sgo.data.netschool.NetSchoolSingleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GosuslugiFragment : Fragment(R.layout.fragment_gosuslugi) {

    private val viewModel by viewModels<GosuslugiViewModel>()

    private val webView = GosuslugiWebView { loginState ->
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.login(
                loginState,
                onOneUser = { userId ->
                    findNavController().navigate(
                        R.id.action_gosuslugiFragment_to_gosuslugiResult,
                        bundleOf(
                            GosuslugiResultFragment.LOGIN_STATE to loginState,
                            GosuslugiResultFragment.USER_ID to userId
                        )
                    )
                },
                onMoreUser = {
                    findNavController().navigate(
                        R.id.action_gosuslugiFragment_to_chooseUserIdFragment,
                        bundleOf(GosuslugiResultFragment.LOGIN_STATE to loginState)
                    )
                }
            )
        }
    }

    private var binding: FragmentGosuslugiBinding? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentGosuslugiBinding.bind(view)


        with(binding!!.root) {
            loadUrl("${NetSchoolSingleton.baseUrl}/webapi/sso/esia/crosslogin?esia_permissions=1&esia_role=1")
            settings.apply {
                loadsImagesAutomatically = true
                javaScriptEnabled = true
            }
            webViewClient = webView
            scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        }
    }


}