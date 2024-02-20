package com.mezhendosina.sgo.domain

import android.content.Context
import androidx.core.os.bundleOf
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

class FirebaseUseCase {
    fun initFirebase(context: Context) {
        FirebaseApp.initializeApp(context)
    }

    fun appOpen() {
        val analytics = Firebase.analytics
        analytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, bundleOf())
    }
}
