package com.ktor.chat

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.io.IOException
import java.net.URL


class FirebaseInstanceIDService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        println("Token is $token")
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMessageReceived(message: RemoteMessage) {
        println("Message Received ")
        if (message.notification != null) {
            println("Message Image is ${message.data["url"]}")
            try {
                val url = URL(message.data["url"])
                val images = BitmapFactory.decodeStream(url.openConnection().getInputStream())
            generate(
                title = message.notification!!.title,
                message = message.notification!!.body,
                image = images
            )
            } catch (e: IOException){
                println()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun generate(title: String?, message: String?, image: Bitmap) {
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
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://ktor.page.link/NLtk"))
        val pendingIntent = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(
                0,
                PendingIntent.FLAG_IMMUTABLE
            )
        }
        pendingIntent.send()
        try {
            val NotificatonBuilder: NotificationCompat.Builder =
                NotificationCompat.Builder(this, channelId)
            NotificatonBuilder.setContentTitle(title)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setStyle(NotificationCompat.BigPictureStyle().bigPicture(image).setBigContentTitle(title))
                .setAutoCancel(true).addAction(
                    R.drawable.ic_launcher_background,
                    "Open Message",
                    pendingIntent
                ).setVibrate(longArrayOf(1000, 1000, 1000, 1000))

            manager.notify(50, NotificatonBuilder.build())
        }
        catch (e: IOException) {
            System.out.println(e)
        }

    }


}

