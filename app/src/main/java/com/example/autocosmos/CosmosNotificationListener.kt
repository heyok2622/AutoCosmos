package com.example.autocosmos

import android.app.Notification
import android.content.Intent
import android.net.Uri
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification

class CosmosNotificationListener : NotificationListenerService() {
    override fun onNotificationPosted(sbn: StatusBarNotification) {
        // 1. 카카오톡 알림인지 확인
        if (sbn.packageName == "com.kakao.talk") {
            val extras = sbn.notification.extras
            val text = extras.getCharSequence(Notification.EXTRA_TEXT)?.toString() ?: ""

            // 2. 텍스트 내부의 코스모스 인증 링크 추출
            val urlRegex = Regex("(https?://go\\.csmsgo\\.com/[^\\s]*)")
            val match = urlRegex.find(text)

            // 3. 링크 발견 시 브라우저 자동 실행
            match?.value?.let { url ->
                val intent = Intent(this, AuthWebViewActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    putExtra("URL", url) // URL 데이터를 함께 넘겨줌
                }
                startActivity(intent)
            }
        }
    }
}