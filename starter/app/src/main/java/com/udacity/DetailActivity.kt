package com.udacity

import android.app.DownloadManager
import android.app.NotificationManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*
import timber.log.Timber

class DetailActivity : AppCompatActivity() {
    private var fileStatus = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)
        cancelNotification()
        val extras = intent.extras

        extras?.let {
            var fileName = it.getString(EXTRA_DOWNLOAD_FILE_NAME)
            var status = it.getInt(EXTRA_DOWNLOAD_STATUS)

            tv_detail_file_name.text = fileName
            when (status) {
                DownloadManager.STATUS_SUCCESSFUL -> {
                    fileStatus = "Successfull"
                    tv_detail_status.setTextColor(getColor(R.color.colorPrimaryDark))
                }
                DownloadManager.STATUS_FAILED -> {
                    fileStatus = "Failed"
                    tv_detail_status.setTextColor(getColor(R.color.colorFailed))
                }
            }

            tv_detail_status.text = fileStatus
        }

        detail_button.setOnClickListener {
            finish()
        }
    }

    private fun cancelNotification() {
        val notificationManager =
            ContextCompat.getSystemService(
                applicationContext,
                NotificationManager::class.java
            ) as NotificationManager
        notificationManager.cancelNotifications()
    }

}
