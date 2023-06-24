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

package com.mezhendosina.sgo.app.ui.loginFlow.requestRegion

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.mezhendosina.sgo.app.R
import com.mezhendosina.sgo.app.databinding.FragmentRequestRegionBinding

class RequestRegionFragment : Fragment(R.layout.fragment_request_region) {

    private lateinit var binding: FragmentRequestRegionBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentRequestRegionBinding.bind(view)

    }
}