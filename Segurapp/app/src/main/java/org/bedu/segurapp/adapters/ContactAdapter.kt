package org.bedu.segurapp.adapters

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.bedu.segurapp.R
import org.bedu.segurapp.databinding.ItemContactBinding
import org.bedu.segurapp.helpers.snackBarMaker
import org.bedu.segurapp.models.NotificationData
import org.bedu.segurapp.models.PushNotification
import org.bedu.segurapp.models.SafeContact
import org.bedu.segurapp.networking.RetrofitInstance



class ContactAdapter(
    private var contactList: MutableList<SafeContact>

) :
    RecyclerView.Adapter<ContactAdapter.ViewHolder>() {




    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(viewGroup)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item = contactList[position]
        viewHolder.bind(item)
    }

    override fun getItemCount(): Int = contactList.size


    class ViewHolder private constructor(val binding: ItemContactBinding) :
        RecyclerView.ViewHolder(binding.root) {



        private val mAuth = Firebase.auth
        private val db = Firebase.firestore
        private val usersCollection = db.collection("users")


        fun bind(item: SafeContact) {

            binding.tvNombre.text = item.name
            binding.tvPhone.text = item.telephone

            val btnAction = binding.btnAction

            if(item.status == "Pending"){
                btnAction.setImageResource(R.drawable.ic_info)
            }else{
                btnAction.setImageResource(R.drawable.ic_call2)
            }

            binding.btnAction.setOnClickListener {


                when (item.status ) {
                    "Approved" -> {
                        val phone = item.telephone
                        startActivity(
                            binding.root.context,
                            Intent(Intent.ACTION_DIAL).setData(Uri.parse("tel:${phone.trim()}")),
                            null
                        )
                    }

                   "Pending" -> {
                       resendNotification(item.name, item.telephone)
                    }
                }
            }

        }

        private fun resendNotification(contactName: String, contactNumber: String){
            getUserInfo(contactName, contactNumber)
        }

        private fun getUserInfo(contactName: String, contactNumber: String){

            mAuth.currentUser?.uid?.let { idUser ->
                usersCollection
                    .whereEqualTo("id", idUser)
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        if (querySnapshot.documents.isNotEmpty()) {
                            val mNumber = querySnapshot.documents.first().get("telephone").toString()
                            val mName = querySnapshot.documents.first().get("name").toString()
                            val channelId = querySnapshot.documents.first().get("channel.id").toString()
                            sendRequest(contactName, contactNumber, mName, mNumber, channelId)
                        }
                    }

            }


        }


        private fun sendRequest(contactName: String, contactNumber: String,  mName: String, mNumber: String, channelId: String) {

            usersCollection
                .whereEqualTo("telephone", contactNumber)
                .get()
                .addOnSuccessListener {
                    if (it.documents.isNotEmpty()) {

                        val recipientToken =
                            it.documents.first().getString("pushNotificationKey")

                        if (recipientToken != null) {
                            PushNotification(
                                NotificationData(
                                    binding.root.context.getString(
                                        R.string.notification_send_request_title,
                                        "$mName ($mNumber)"
                                    ),
                                    binding.root.context.getString(R.string.notification_request_description),
                                    channelId
                                ),
                                recipientToken
                            ).also { pushNotification ->
                                sendNotification(pushNotification)
                            }
                        }

                    } else {
                        Toast.makeText(
                            binding.root.context,
                            "$contactName no cuenta con SegurApp",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }


                }

                .addOnFailureListener { exception ->
                    Toast.makeText(
                        binding.root.context,
                        "${exception.message}",
                        Toast.LENGTH_SHORT
                    )
                        .show()


                }
        }

        private fun sendNotification(notification: PushNotification) = CoroutineScope(
            Dispatchers.IO).launch {
            try {
                val response = RetrofitInstance.api.postNotification(notification)
                if (response.isSuccessful) {
                    Handler(Looper.getMainLooper()).post {
                        snackBarMaker(binding.root.context, binding.root, R.string.send_notification_title, Snackbar.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e(ContentValues.TAG, response.errorBody().toString())
                }
            } catch (e: Exception) {
                Log.e(ContentValues.TAG, e.toString())
            }
        }



        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemContactBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    fun filterListC(filteredListC: MutableList<SafeContact>) {
        contactList = filteredListC
        notifyDataSetChanged()
    }


}

