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

package com.mezhendosina.sgo.app.utils

import android.view.View
import androidx.transition.TransitionManager
import com.mezhendosina.sgo.app.databinding.ItemLessonToolbarBinding
import kotlin.math.abs

fun ItemLessonToolbarBinding.addOnToolbarCollapseListener(emoji: Int?) {
    val fadeTransition = com.google.android.material.transition.MaterialFadeThrough()
    this.appbarlayout.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
        TransitionManager.beginDelayedTransition(this.root, fadeTransition)
        collapsedIcon.visibility =
            if (abs(verticalOffset) - appBarLayout.totalScrollRange == 0 && emoji != null) {
                View.VISIBLE
            } else {
                View.INVISIBLE
            }
    }
}

fun ItemLessonToolbarBinding.setup(emoji: Int?) {

    if (emoji != null) {
        expandedIcon.setImageResource(emoji)
        collapsedIcon.setImageResource(emoji)
//        if (collapsingtoolbarlayout.lineCount > 1) {
//
//        }
    } else {
        expandedIcon.visibility = View.GONE
        collapsedIcon.visibility = View.GONE
    }
}