package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import timber.log.Timber


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0
    private var optSelectedStr = ""

    private lateinit var notificationManager: NotificationManager
    private lateinit var downloadManager: DownloadManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        Timber.plant(Timber.DebugTree())

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        createChannel(CHANNEL_ID, CHANNEL_NAME)

        custom_button.setOnClickListener {
            when (radiogroup_options.checkedRadioButtonId) {
                R.id.radiobutton_glide -> {
                    optSelectedStr = getString(R.string.option_glide)
                    download(URL_GLIDE)
                    custom_button.setButtonState(ButtonState.Loading)
                }
                R.id.radiobutton_load_app -> {
                    optSelectedStr = getString(R.string.option_load_app)
                    download(URL_LOAD_APP)
                    custom_button.setButtonState(ButtonState.Loading)
                }
                R.id.radiobutton_retrofit -> {
                    optSelectedStr = getString(R.string.option_retrofit)
                    download(URL_RETROFIT)
                    custom_button.setButtonState(ButtonState.Loading)
                }
                else -> {
                    Toast.makeText(
                        this,
                        getString(R.string.select_file),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            id.let {

                //Query the download manager about downloads that have been requested.
                val query = DownloadManager.Query()
                    .setFilterById(id!!)
                val cursor = downloadManager.query(query)

                // Get the download status
                if (cursor.moveToFirst()) {
                    var status = cursor.getInt(
                        cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
                    )

                    notificationManager.sendNotification(
                        applicationContext,
                        id!!.toInt(),
                        CHANNEL_ID,
                        optSelectedStr,
                        status
                    )
                }
            }
        }
    }

    private fun download(url: String) {
        val request =
            DownloadManager.Request(Uri.parse(url))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
    }

    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW)
            notificationChannel.description = "Download Finished"

            notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    companion object {
        private const val URL_GLIDE =
            "https://github.com/bumptech/glide/archieve/master.zip"

        private const val URL_LOAD_APP =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"

        private const val URL_RETROFIT =
            "https://github.com/square/retrofit/archive/master.zip"

        private const val CHANNEL_ID = "channelId"
        private const val CHANNEL_NAME = "channelName"
    }

}
