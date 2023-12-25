package com.vinod.beaconads

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.Observer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.altbeacon.beacon.Beacon
import org.altbeacon.beacon.BeaconParser
import org.altbeacon.beacon.Region
import org.altbeacon.beacon.utils.UrlBeaconUrlCompressor

class BeaconScannerService : Service() {
    private val region: Region = Region(
        "backgroundRegion", null, null, null
    )

    val ignoreList = HashSet<String>()

    private val rangingObserver = Observer<Collection<Beacon>> { beacons ->
        Log.d("TAG", "Vinod: ${beacons.count()} beacons")
        for (beacon: Beacon in beacons) {
            if (beacon.serviceUuid == 0xfeaa && beacon.beaconTypeCode == 0x10 && !ignoreList.contains(
                    beacon.id1.toString()
                )
            ) {
                Log.d("TAG", "Vinod: ${UrlBeaconUrlCompressor.uncompress(beacon.id1.toByteArray())} beacons")
                if (UrlBeaconUrlCompressor.uncompress(beacon.id1.toByteArray()) == "https://www.vinodkumarc.com") {
                    openNewActivity(UrlBeaconUrlCompressor.uncompress(beacon.id1.toByteArray()))
                    showNotification(UrlBeaconUrlCompressor.uncompress(beacon.id1.toByteArray()))
                    Log.d("TAG", "Vinod: beacons opened ${UrlBeaconUrlCompressor.uncompress(beacon.id1.toByteArray())}")
                }
                ignoreList.add(beacon.id1.toString())
            }
        }
    }

    private fun openNewActivity(url: String) {
        Intent(this, WebViewActivity::class.java).run {
            putExtra("urlToLoad", url).flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(this)
        }
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        startBeaconListener()
    }

    private fun startBeaconListener() {
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main) {
                BeaconApplication.getInstance().beaconManager.getRegionViewModel(region).rangedBeacons.observeForever(
                    rangingObserver
                )
            }
            BeaconApplication.getInstance().apply {
                beaconManager.beaconParsers
                    .add(BeaconParser().setBeaconLayout(BeaconParser.EDDYSTONE_URL_LAYOUT))
                beaconManager.beaconParsers
                    .add(BeaconParser().setBeaconLayout(BeaconParser.EDDYSTONE_UID_LAYOUT))
                beaconManager.startMonitoring(region)
                beaconManager.startRangingBeacons(region)
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startBeaconListener()
        return START_STICKY
    }

    @SuppressLint("MissingPermission")
    fun showNotification(url: String) {
        val notificationIntent = Intent(this, WebViewActivity::class.java)
        notificationIntent.putExtra("urlToLoad", url)
        val contentIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )


        // Build the notification
        val notificationBuilder = NotificationCompat.Builder(this, "my_channel_id")
            .setSmallIcon(org.altbeacon.beacon.R.drawable.navigation_empty_icon)
            .setContentTitle("Notification Title")
            .setContentText("Notification Content")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(contentIntent)
            .setAutoCancel(true) // Close the notification when clicked

        // Show the notification
        val notificationManager = NotificationManagerCompat.from(this)
        notificationManager.notify(1, notificationBuilder.build())
    }


    override fun onDestroy() {
        super.onDestroy()
        try {
            BeaconApplication.getInstance().beaconManager.stopMonitoring(region)
            BeaconApplication.getInstance().beaconManager.stopRangingBeacons(region)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}