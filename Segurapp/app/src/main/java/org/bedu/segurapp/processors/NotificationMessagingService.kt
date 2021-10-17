package org.bedu.segurapp.processors

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.support.annotation.RequiresApi
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.bedu.segurapp.R
import org.bedu.segurapp.ui.TrackingActivity
import org.bedu.segurapp.ui.request.RequestActivity
import kotlin.random.Random


private var CHANNEL_ID = "my_channel"

class NotificationMessagingService : FirebaseMessagingService() {
    private val db = Firebase.firestore
    var preferences: SharedPreferences? = null
    var preferencesEditor: SharedPreferences.Editor? = null
    @SuppressLint("CommitPrefEdits")
    override fun onNewToken(token: String) {

        val preferencesName = getString(R.string.shared_preferences)
        preferences = this.getSharedPreferences(preferencesName, Context.MODE_PRIVATE)
        preferencesEditor = preferences?.edit()
        preferencesEditor?.putString(
            getString(R.string.shared_preferences_firebase_id),
            token
        )

        preferencesEditor?.apply()
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        val preferencesName = getString(R.string.shared_preferences)
        preferences = this.getSharedPreferences(preferencesName, Context.MODE_PRIVATE)
        val userId = preferences!!.getString(getString(R.string.shared_preferences_user_id),  null)

        if (userId != null && userId > "-1") {
            lateinit var userTelephone : String
            getUserFromDb(userId) { telephone ->
                if (telephone != ""){
                    userTelephone = telephone
                    if (remoteMessage.data.isNotEmpty()) {

                        val intent: Intent

                        if(remoteMessage.data["channelId"] == null){
                            // Request to add
                            intent = Intent(this@NotificationMessagingService, RequestActivity::class.java)
                            intent.putExtra("title", remoteMessage.data["title"])
                            intent.putExtra("url", remoteMessage.data["url"])
                            intent.putExtra("userTelephone", userTelephone)
                        }else{
                            // Request for help
                            intent = Intent(this@NotificationMessagingService, TrackingActivity::class.java)
                            CHANNEL_ID = remoteMessage.data["channelId"].toString()
                        }

                        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                        val notificationID = Random.nextInt()

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            createNotificationChannel(notificationManager)
                        }

                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        val pendingIntent = PendingIntent.getActivity(this, 0, intent, FLAG_ONE_SHOT)
                        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
                            .setContentTitle(remoteMessage.data["title"])
                            .setContentText(remoteMessage.data["message"])
                            .setSmallIcon(R.mipmap.ic_launcher_round)
                            .setAutoCancel(true)
                            .setContentIntent(pendingIntent)
                            .build()

                        notificationManager.notify(notificationID, notification)
                    }
                }
            }
        }
    }



    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channelName = "channelName"
        val channel = NotificationChannel(CHANNEL_ID, channelName, IMPORTANCE_HIGH).apply {
            description = "My channel description"
            enableLights(true)
            lightColor = Color.RED
        }
        notificationManager.createNotificationChannel(channel)
    }


    private fun getUserFromDb(userId: String, callback: (String) -> Unit){
        db.collection("users")
            .whereEqualTo("id", userId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.documents.isNotEmpty()) {
                    val telephone = querySnapshot.documents[0].get("telephone").toString()
                    callback(telephone)
                }
            }

            .addOnFailureListener { exception ->
                Toast.makeText(
                    baseContext,
                    "${exception.message}",
                    Toast.LENGTH_SHORT
                ).show()

                callback("")
            }

    }
}