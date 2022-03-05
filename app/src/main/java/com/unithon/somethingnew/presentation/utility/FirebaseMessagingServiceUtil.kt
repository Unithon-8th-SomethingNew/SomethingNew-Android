package com.unithon.somethingnew.presentation.utility

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.unithon.somethingnew.R
import com.unithon.somethingnew.presentation.call.NokeActivity

class FirebaseMessagingServiceUtil : FirebaseMessagingService() {
    val TAG = "FirebaseMessagingServiceUtil.class"

    @SuppressLint("LongLogTag")
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "onNewToken: ${token}")
    }

    /**
     * 디바이스가 FCM을 통해서 메시지를 받으면 수행된다.
     * @remoteMessage: FCM에서 보낸 데이터 정보들을 저장한다.
     */
    @SuppressLint("LongLogTag")
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        Log.d("dddd","ddddd1")
        // FCM을 통해서 전달 받은 정보에 Notification 정보가 있는 경우 알림을 생성한다.
        if (remoteMessage.notification != null) {
            // 1. Vibrator 객체를 얻어온 다음
            Log.d("dddd", remoteMessage.notification!!.body!!)
            sendNotification(remoteMessage)

        } else {
            Log.d(TAG, "수신 에러: Notification이 비어있습니다.")
        }
    }

    /**
     * FCM에서 보낸 정보를 바탕으로 디바이스에 Notification을 생성한다.
     * @remoteMessage: FCM에서 보
     */
    private fun sendNotification(remoteMessage: RemoteMessage) {
        val id = 0
        var title = remoteMessage.notification!!.title
        var name = remoteMessage.notification!!.body?.split("-")?.get(0)
        var uid = remoteMessage.notification!!.body?.split("-")?.get(1)
        var profileUrl = remoteMessage.notification!!.body?.split("-")?.get(2)

        var intent = Intent(this, NokeActivity::class.java)
        intent.putExtra("channelId", uid)
        intent.putExtra("name", name)
        intent.putExtra("profileUrl", profileUrl)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, id, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val channelId = "Channel ID"
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText("${name}님이 찾아왔어요!")
            .setAutoCancel(true)
            .setSound(soundUri)
            .setContentIntent(pendingIntent)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(channelId, "Notice", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(id, notificationBuilder.build())
    }
}