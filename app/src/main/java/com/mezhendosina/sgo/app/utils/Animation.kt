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

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.transition.TransitionManager
import android.view.View
import com.google.android.material.transition.platform.MaterialSharedAxis
import com.mezhendosina.sgo.app.databinding.ContainerMainBinding

fun showAnimation(view: View) {
    view.apply {
        alpha = 0f
        visibility = View.VISIBLE
        animate().alpha(1f).setDuration(300).start()
    }
}

fun hideAnimation(
    view: View,
    endVisibility: Int,
) {
    view.animate()
        .alpha(0f)
        .setDuration(300)
        .setListener(
            object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    view.visibility = endVisibility
                }
            },
        )
}

fun ContainerMainBinding.slideDownAnimation() {
    apply {
        val materialFade = MaterialSharedAxis(MaterialSharedAxis.Y, false)
        TransitionManager.beginDelayedTransition(appbarLayout, materialFade)
        tabsLayout.visibility = View.VISIBLE
        gradesTopBar.root.visibility = View.GONE
    }
}

fun ContainerMainBinding.slideUpAnimation() {
    val materialFade = MaterialSharedAxis(MaterialSharedAxis.Y, true)
    TransitionManager.beginDelayedTransition(appbarLayout, materialFade)
    tabsLayout.visibility = View.GONE
    gradesTopBar.root.visibility = View.VISIBLE
}
