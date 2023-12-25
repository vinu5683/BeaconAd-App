package com.vinod.beaconads

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import org.altbeacon.beacon.BeaconManager

class BeaconApplication : Application() {

    lateinit var beaconManager: BeaconManager

    override fun onCreate() {
        super.onCreate()
        beaconApplication = this
        beaconManager = BeaconManager.getInstanceForApplication(this)
        //BeaconManager.setDebug(true)
        createNotificationChannel()
    }


    companion object {
        private lateinit var beaconApplication: BeaconApplication
        fun getInstance(): BeaconApplication {
            return beaconApplication
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "my_channel_id"
            val channelName = "My Channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, channelName, importance)
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun clearAllNotifications() {
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.cancelAll()
    }

}