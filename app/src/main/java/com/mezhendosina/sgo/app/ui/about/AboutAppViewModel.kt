package com.mezhendosina.sgo.app.ui.about

import androidx.lifecycle.ViewModel
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.mezhendosina.sgo.Singleton
import com.mezhendosina.sgo.data.Settings
import com.mezhendosina.sgo.data.requests.notifications.NotificationsSource
import com.mezhendosina.sgo.data.requests.notifications.entities.NotificationUserEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class AboutAppViewModel : ViewModel() {

    fun registerUser() {
        val url = FirebaseRemoteConfig.getInstance().getString("notifications_server")

        val notificationsSource = NotificationsSource("http://45.143.92.29:8000/")

        val settings = Settings(Singleton.getContext())
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            CoroutineScope(Dispatchers.IO).launch {
                val loginData = settings.getLoginData()
                val user = NotificationUserEntity(
                    settings.currentUserId.first(),
                    it.result,
                    loginData.UN,
                    loginData.PW,
                    settings.regionUrl.first()?.dropLast(1) ?: "",
                    loginData.cid,
                    loginData.sid,
                    loginData.pid,
                    loginData.cn,
                    loginData.sft,
                    loginData.scid,
                    1
                )
                notificationsSource.notificationsSource.registerUser(user)
            }
        }
    }
}