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
import com.unithon.somethingnew.presentation.main.MainActivity

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

        // FCM을 통해서 전달 받은 정보에 Notification 정보가 있는 경우 알림을 생성한다.
        if (remoteMessage.notification != null) {
            // 1. Vibrator 객체를 얻어온 다음
            try {
                val vibrator: Vibrator
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as Vibrator
                    vibrator.vibrate(VibrationEffect.createOneShot(1000, 100))
                } else {
                    @Suppress("DEPRECATION")
                    vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                    @Suppress("DEPRECATION")
                    vibrator.vibrate(1000) // 1초간 진동
                }

                sendNotification(remoteMessage)
            } catch (e: Exception) {

            }
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
        var name = remoteMessage.notification!!.title?.split("-")?.get(0)
        var body = remoteMessage.notification!!.body
        var uid = remoteMessage.notification!!.title?.split("-")?.get(1)
        var profileUrl = remoteMessage.notification!!.title?.split("-")?.get(2)

        var intent = Intent(this, MainActivity::class.java)
        intent.putExtra("channelId", uid)
        intent.putExtra("name", name)
        intent.putExtra("profileUrl", profileUrl)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, id, intent, PendingIntent.FLAG_ONE_SHOT)

        val channelId = "Channel ID"
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("${name}님이 찾아왔어요!")
            .setContentText(body)
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