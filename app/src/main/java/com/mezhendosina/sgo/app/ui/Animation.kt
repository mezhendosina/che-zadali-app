package com.mezhendosina.sgo.app.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View


fun showAnimation(view: View) {
    view.apply {
        alpha = 0f
        visibility = View.VISIBLE
        animate().alpha(1f).setDuration(300).start()
    }
}

fun hideAnimation(view: View, endVisibility: Int) {
    view.animate()
        .alpha(0f)
        .setDuration(300)
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                view.visibility = endVisibility
            }
        })
}