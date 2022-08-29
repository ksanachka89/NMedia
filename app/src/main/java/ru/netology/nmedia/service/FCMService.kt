package ru.netology.nmedia.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import ru.netology.nmedia.R
import kotlin.random.Random

class FCMService : FirebaseMessagingService() {
    private val gson = Gson()

    override fun onCreate() { // регистрация канала
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_remote_name)
            val descriptionText = getString(R.string.channel_remote_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    override fun onMessageReceived(message: RemoteMessage) { // будет вызываться, когда приходит пуш
        val data = message.data
        val serializedAction = data[Action.KEY] ?: return // если не было action, закончим обработку
        val action = Action.values().find { // среди всех значений action найти то, у ктр ключ
            it.key == serializedAction
        } ?: return

        when (action) {
            Action.Like -> handleLikeAction(data[CONTENT_KEY] ?: return)
            Action.NewPost -> handleNewPostAction(data[CONTENT_KEY] ?: return)
        }
    }

    override fun onNewToken(token: String) { // при старте приложения получим токен
        super.onNewToken(token)
        Log.d("onNewToken", token)
    }

    private fun handleLikeAction(serializedContent: String) { // отправка notification
        val likeContent = gson.fromJson(serializedContent, Like::class.java)
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(
                getString(
                    R.string.notification_user_liked,
                    likeContent.userName,
                    likeContent.postAuthor
                )
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
        NotificationManagerCompat.from(this) // показать
            .notify(Random.nextInt(100_000), notification)
    }

    private fun handleNewPostAction(serializedContent: String) {
        val newPostContent = gson.fromJson(serializedContent, NewPost::class.java)
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(
                getString(
                    R.string.notification_user_new_post,
                    newPostContent.userName
                )
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentText("${newPostContent.contentText.substring(0, 20)}...")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(newPostContent.contentText)
            )
            .build()

        NotificationManagerCompat.from(this)
            .notify(Random.nextInt(100_000), notification)
    }

    private companion object {
        const val CONTENT_KEY = "content"
        const val CHANNEL_ID = "remote"
    }
}