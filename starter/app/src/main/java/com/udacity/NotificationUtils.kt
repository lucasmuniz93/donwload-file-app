package com.udacity

import android.app.DownloadManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

// Notification ID.

private val NOTIFICATION_ID = 0
private lateinit var action: NotificationCompat.Action
private lateinit var pendingIntent: PendingIntent

const val EXTRA_DOWNLOAD_STATUS = "extra_download_status"
const val EXTRA_DOWNLOAD_FILE_NAME = "extra_download_file_name"

fun NotificationManager.sendNotification(
    context: Context,
    downloadId: Int,
    channelId: String,
    contentFileName: String,
    downloadStatus: Int
) {

    val contentIntent = Intent(context, DetailActivity::class.java)
    contentIntent.putExtra(EXTRA_DOWNLOAD_FILE_NAME, contentFileName)
    contentIntent.putExtra(EXTRA_DOWNLOAD_STATUS, downloadStatus)
    pendingIntent = PendingIntent.getActivity(
        context,
        NOTIFICATION_ID,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    // Create the action for the notification
    action = NotificationCompat.Action(
        null,
        context.getString(R.string.view_details),
        pendingIntent
    )

    val builder =
        NotificationCompat.Builder(context, channelId)
            .setContentTitle(context.resources.getString(R.string.notification_title))
            .setContentText(context.resources.getString(R.string.notification_description))
            .setSmallIcon(R.drawable.ic_baseline_cloud_download_24)
            .setContentIntent(pendingIntent)
            .addAction(action)



    notify(downloadId, builder.build())
}

fun NotificationManager.cancelNotifications() {
    cancelAll()
}