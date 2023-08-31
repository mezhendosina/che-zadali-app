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

import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient

class GosuslugiWebView(private val onLoggedIn: (loginState: String) -> Unit) : WebViewClient() {

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        val url = request?.url
        if (url?.query?.contains("loginState=") == true) {
            val query = url.query?.removePrefix("loginState=")
            if (query != null) onLoggedIn.invoke(query)
        }
        return super.shouldOverrideUrlLoading(view, request)

    }
}