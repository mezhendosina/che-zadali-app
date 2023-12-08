package com.mezhendosina.sgo.app.ui.loginFlow

import android.widget.FrameLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding


fun FrameLayout.setOnInsetChanges() {
    ViewCompat.setOnApplyWindowInsetsListener(this) { view, windowInsets ->
        val insets = windowInsets.getInsets(WindowInsetsCompat.Type.ime())

        view.updatePadding(
            insets.left,
            insets.top,
            insets.right,
            insets.bottom
        )
        WindowInsetsCompat.CONSUMED
    }
}