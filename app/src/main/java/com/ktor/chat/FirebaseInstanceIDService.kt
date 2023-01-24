package com.ktor.chat

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class FirebaseInstanceIDService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        println("Token is $token")
    }


    override fun onMessageReceived(message: RemoteMessage) {
        println("Message Received ")
        if (message.notification != null) {
            generate(title = message.notification!!.title, message = message.notification!!.body)
        }
    }

    fun generate(title: String?, message: String?) {
        val channelId = "Notify"
        val channelName = "Push Notifications"
        val manager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val NotificatonChannel: NotificationChannel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_HIGH
        )
        manager.createNotificationChannel(NotificatonChannel)
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent =
            PendingIntent.getActivity(this, 100, intent, PendingIntent.FLAG_IMMUTABLE)
        val NotificatonBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, channelId)
        NotificatonBuilder.setContentTitle(title)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentText(message)
            .setAutoCancel(true).addAction(
                R.drawable.ic_launcher_background,
                "Open Message",
                pendingIntent
            ).setVibrate(longArrayOf(1000, 1000, 1000, 1000))

        manager.notify(50, NotificatonBuilder.build())

    }


}

