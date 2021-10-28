package org.bedu.segurapp.ui.request

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import org.bedu.segurapp.R
import org.bedu.segurapp.databinding.ActivityRequestBinding
import org.bedu.segurapp.models.Channel
import org.bedu.segurapp.models.SubscribedTo
import org.json.JSONObject


class RequestActivity : AppCompatActivity() {

    private val db = Firebase.firestore
    private val usersCollection = db.collection("users")
    private val binding by lazy {ActivityRequestBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        pushNotificationListener()
    }

    private fun pushNotificationListener() {

        val title = intent.getStringExtra("title").toString()
        val request = intent.getStringExtra("url")
        val mUserTelephone = intent.getStringExtra("userTelephone").toString()
        request?.let {
            initView(title, it, mUserTelephone)
        }
    }

    private fun initView(title: String, channel: String, mUserTelephone: String) {

        binding.tvTitle.text = title
        binding.btnAccept.setOnClickListener {
            updateResponse(channel, mUserTelephone) { response ->
                if (response) {
                    val result = title.trim().split("(")
                    Toast.makeText(
                        this,
                        getString(R.string.accept_request_message, result.first()),
                        Toast.LENGTH_SHORT
                    ).show()
                    FirebaseMessaging.getInstance().subscribeToTopic(channel)
                    closeActivity()
                }
            }
        }

        binding.btnDecline.setOnClickListener {
            Toast.makeText(this, getString(R.string.deny_request_message), Toast.LENGTH_SHORT)
                .show()
            closeActivity()
        }

    }


    private fun updateResponse(
        channel: String,
        telephone: String,
        callback: (Boolean) -> Unit
    ) {

       usersCollection
            .whereEqualTo("channel.id", channel)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.documents.isNotEmpty()) {

                    val data = querySnapshot.documents.first().get("channel") as HashMap<*, *>
                    val userId = querySnapshot.documents.first().get("id") as String
                    val mChannel = Gson().fromJson(JSONObject(data).toString(), Channel::class.java)
                    for (item in mChannel.members) {
                        if (item.telephone == telephone) {
                            item.status = "Approved"
                            break
                        }
                    }


                    getSubscribedToList(telephone, mChannel.id) { subscribedResponse ->
                        if (subscribedResponse) {
                            userId.let { documentResponse ->
                                usersCollection
                                    .document(documentResponse)
                                    .update("channel", mChannel)
                                    .addOnCompleteListener { callback(it.isSuccessful) }
                            }
                        }
                    }


                }
            }

            .addOnFailureListener { exception ->
                Toast.makeText(
                    baseContext,
                    "${exception.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }


    }


    private fun getSubscribedToList(
        telephone: String,
        channel: String,
        callback: (Boolean) -> Unit
    ) {

        usersCollection
            .whereEqualTo("telephone", telephone)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.documents.isNotEmpty()) {
                    val subscribedData = querySnapshot.documents.first().get("subscribedToList")
                    val mSubscribedToList = Gson().fromJson(
                        subscribedData.toString(),
                        Array<SubscribedTo>::class.java
                    ).toMutableList()

                    mSubscribedToList.add(SubscribedTo(channel))

                    getUserId(mSubscribedToList, telephone) {
                        callback(it)
                    }
                }
            }

            .addOnCompleteListener {

                callback(it.isSuccessful)
            }

    }


    private fun getUserId(
        subscribedToList: MutableList<SubscribedTo>,
        telephone: String,
        callback: (Boolean) -> Unit
    ) {
       usersCollection
            .whereEqualTo("telephone", telephone)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.documents.isNotEmpty()) {
                    val idUser = querySnapshot.documents.first().get("id").toString()

                    updateSubscribedToList(subscribedToList, idUser){
                        callback(it)
                    }
                }

            }

    }


    private fun updateSubscribedToList(
        subscribedToList: MutableList<SubscribedTo>,
        idUser: String,
        callback: (Boolean) -> Unit
    ) {
        usersCollection
            .document(idUser)
            .update("subscribedToList", subscribedToList)
            .addOnCompleteListener {
                callback(it.isSuccessful)
            }
    }


    private fun closeActivity() {
        finish()
    }

}