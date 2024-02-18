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

package com.mezhendosina.sgo.app.ui.main.container

import com.mezhendosina.sgo.app.databinding.FragmentGradesBinding

interface GradesActionsInterface {
    fun setupGrades()

    fun observeGrades()

    fun observeGradesError()

    fun observeGradesLoadState()

    fun FragmentGradesBinding.showGrades()

    fun FragmentGradesBinding.showEmptyState()

    fun FragmentGradesBinding.showLoading()

    fun FragmentGradesBinding.showError()
}
