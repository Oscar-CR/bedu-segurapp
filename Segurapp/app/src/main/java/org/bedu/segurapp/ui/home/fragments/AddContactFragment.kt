package org.bedu.segurapp.ui.home.fragments

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.bedu.segurapp.R
import org.bedu.segurapp.databinding.FragmentAddContactBinding
import org.bedu.segurapp.helpers.makeFormValidations
import org.bedu.segurapp.models.*
import org.bedu.segurapp.networking.RetrofitInstance
import org.json.JSONObject

class AddContactFragment : Fragment() {

    private lateinit var binding: FragmentAddContactBinding
    private lateinit var mChannel: Channel

    private val mAuth = Firebase.auth
    private val db = Firebase.firestore
    private val usersCollection = db.collection("users")

    private val userId = mAuth.currentUser?.uid
    private var channelId: String? = null
    private var mNumber: String? = null
    private var mName: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_add_contact,container, false)
        getUserInfo()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding){

            setAnimation()

            btnSaveContact.setOnClickListener{

                if(mName != null && mNumber != null) {
                    if (makeFormValidations(arrayOf(etUserAdd, etPhoneAdd), requireContext())) {
                        sendRequest(
                            etUserAdd.text.toString(),
                            etPhoneAdd.text.toString(),
                            mName ?: "",
                            mNumber ?: ""
                        )
                    }
                }
            }
        }
    }

    private fun getUserInfo(){

        userId?.let { idUser ->
            usersCollection
                .whereEqualTo("id", idUser)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    if (querySnapshot.documents.isNotEmpty()) {
                        mNumber = querySnapshot.documents.first().get("telephone").toString()
                        mName = querySnapshot.documents.first().get("name").toString()
                        channelId = querySnapshot.documents.first().get("channel.id").toString()
                    }
                }

        }


    }

    private fun setAnimation() {
        with(binding){
            loginAnimation.setAnimation("user.json")
            loginAnimation.playAnimation()
        }

    }

    private fun saveContacts(newSafeContact: SafeContact, callback: (Boolean) -> Unit) {

        userId?.let{ idUser ->

            usersCollection
                .whereEqualTo("id", idUser)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    if (querySnapshot.documents.isNotEmpty()) {

                        val data = querySnapshot.documents.first().get("channel") as HashMap<*, *>

                        mChannel = Gson().fromJson(JSONObject(data).toString(), Channel::class.java)
                        val userMatchList = mChannel.members.filter { it.telephone == newSafeContact.telephone }

                        if(userMatchList.isEmpty()){
                            mChannel.members.add(newSafeContact)
                            usersCollection
                                .document(idUser)
                                .update("channel", mChannel)
                                .addOnCompleteListener { callback(it.isSuccessful) }
                        }else callback(true)
                    }
                }

                .addOnFailureListener { exception ->
                    Toast.makeText(
                        requireContext(),
                        "${exception.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }

    }

    private fun sendRequest(contactName: String, contactNumber: String,  mName: String, mNumber: String) {
        val newSafeContact = SafeContact(contactName, contactNumber)
        val pbLoading = binding.pbLoading

        pbLoading.visibility = View.VISIBLE

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
                                getString(
                                    R.string.notification_send_request_title,
                                    "$mName ($mNumber)"
                                ),
                                getString(R.string.notification_request_description),
                                channelId.toString()
                            ),
                            recipientToken
                        ).also { pushNotification ->
                            sendNotification(pushNotification, newSafeContact)
                        }
                    }

                } else {
                    Toast.makeText(
                        requireContext(),
                        "$contactName no cuenta con SegurApp",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }

                pbLoading.visibility = View.GONE

            }

            .addOnFailureListener { exception ->
                Toast.makeText(
                    requireContext(),
                    "${exception.message}",
                    Toast.LENGTH_SHORT
                )
                    .show()


                pbLoading.visibility = View.GONE
            }
    }

    private fun sendNotification(notification: PushNotification, newSafeContact: SafeContact) = CoroutineScope(Dispatchers.IO).launch {
            try {
                print(notification.to)

                val response = RetrofitInstance.api.postNotification(notification)
                if (response.isSuccessful) {

                    requireActivity().runOnUiThread {
                        saveContacts(newSafeContact) { saveContactResponse ->
                            if (saveContactResponse) {
                                Toast.makeText(
                                    requireContext(),
                                    "${newSafeContact.name} será invitado a tus contactos de confianza",
                                    Toast.LENGTH_SHORT
                                ).show()

                                Navigation.findNavController(requireView()).navigate(R.id.navigateToContactsFragment)
                            }

                        }
                    }
                } else {
                    Log.e(ContentValues.TAG, response.errorBody().toString())
                }
            } catch (e: Exception) {
                Log.e(ContentValues.TAG, e.toString())
            }
        }

}