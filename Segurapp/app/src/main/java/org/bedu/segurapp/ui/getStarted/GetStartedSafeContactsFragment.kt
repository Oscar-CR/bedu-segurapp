package org.bedu.segurapp.ui.getStarted

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.content.res.AppCompatResources.getColorStateList
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.snackbar.Snackbar
import org.bedu.segurapp.R
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.araujo.jordan.excuseme.ExcuseMe
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.bedu.segurapp.UserLogin
import org.bedu.segurapp.helpers.*
import org.bedu.segurapp.models.*
import org.bedu.segurapp.networking.RetrofitInstance
import org.bedu.segurapp.utils.getNameByUri
import org.bedu.segurapp.utils.getPhoneByUri

class GetStartedSafeContactsFragment : Fragment() {

    private lateinit var mPager: ViewPager2
    private lateinit var chipsContainer: ChipGroup
    private lateinit var btnSafeContacts: Button
    private lateinit var btnNext: Button
    private lateinit var txtContactName: EditText
    private lateinit var txtNumber: EditText


    private val safeContactsList: MutableList<Contacts> = ArrayList()

    private lateinit var mChannel: Channel
    private lateinit var mMessage: String
    private var userId: String? = null
    private var channelId: String? = null
    private var mNumber: String? = null
    private var mName: String? = null
    private val db = Firebase.firestore
    private val usersCollection = db.collection("users")

    override fun onResume() {
        super.onResume()
        if (isAValidUser()) setMessage(mMessage) {}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_get_started_safe_contacts, container, false)
        initComponents(view)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        userId = requireActivity().intent.getStringExtra("userId")
        channelId = requireActivity().intent.getStringExtra("channelId")
        if (userId != null && channelId != null) {
            getUserInfo()
            btnSafeContactsClickListener()
            txtNumberActionListener()
            btnNextClickListener()
        }
    }

    override fun onPause() {
        if (safeContactsList.isEmpty() && mPager.currentItem == 3) {
            movePrevious(mPager)
            setSafeContactMessage()
        }

        super.onPause()
    }

    private val resultLauncher =
        this.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intentResult: Intent? = result.data
                intentResult?.data?.let {
                    renderContact(it)
                }
            }
        }

    private fun initComponents(view: View) {
        mPager = (activity as GetStartedActivity).findViewById(R.id.pager)
        chipsContainer = view.findViewById(R.id.chips_container)
        btnSafeContacts = view.findViewById(R.id.btn_safe_contacts)
        btnNext = view.findViewById(R.id.btn_next)
        txtContactName = view.findViewById(R.id.txt_contact_name)
        txtNumber = view.findViewById(R.id.txt_number)
    }

    private fun btnSafeContactsClickListener() {
        btnSafeContacts.setOnClickListener {
            ExcuseMe.couldYouGive(this).permissionFor(
                Manifest.permission.READ_CONTACTS,
            ) {
                if (it.granted.contains(Manifest.permission.READ_CONTACTS)) {
                    openContactsList()
                } else {
                    snackBarMaker(
                        requireContext(),
                        requireView(),
                        R.string.denied_read_contacts_permission_message_hint,
                        Snackbar.LENGTH_INDEFINITE
                    ).setAction(getString(R.string.permission_configuration_hint)) {
                        openAppPermissionsScreen(requireActivity())
                    }.show()
                }
            }
        }
    }

    private fun openContactsList() {
        val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
        return resultLauncher.launch(intent)
    }


    private fun txtNumberActionListener() {
        txtNumber.setOnEditorActionListener(OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (makeFormValidations(arrayOf(txtContactName, txtNumber), requireContext())) {
                    if(mName != null && mNumber != null){
                        sendRequest(txtContactName.text.toString(), txtNumber.text.toString(), mName ?: "", mNumber ?: "")
                        txtNumber.hideKeyboard()
                    }
                }
                return@OnEditorActionListener true
            }
            false
        })
    }

    private fun btnNextClickListener() {
        btnNext.setOnClickListener {
            if (safeContactsList.isNotEmpty()) moveNext(mPager)
            else setSafeContactMessage()
        }
    }

    private fun addSafeContact(safeContact: Contacts) {
        safeContactsList.add(safeContact)
        updateContactList()

        chipsContainer.addView(createChip(safeContact))
        clearForm(arrayOf(txtContactName, txtNumber))
    }

    private fun createChip(safeContact: Contacts): Chip {
        val foregroundColor = ContextCompat.getColor(requireContext(), R.color.segurapp_black)
        val chip = Chip(requireContext())
        chip.text = safeContact.name
        chip.isClickable = true
        chip.isCheckable = false
        chip.isCloseIconVisible = true
        chip.isCheckable = false
        chip.chipBackgroundColor = getColorStateList(requireContext(), R.color.segurapp_gray)
        chip.setTextColor(foregroundColor)
        chip.setOnCloseIconClickListener {
            safeContactsList.remove(safeContact)
            chipsContainer.removeView(chip)
        }
        return chip
    }

    private fun setSafeContactMessage() {
        snackBarMaker(
            requireContext(),
            requireView(),
            R.string.required_safe_contacts_hint,
            Snackbar.LENGTH_LONG
        )
            .show()
    }

    private fun sendRequest(contactName: String, contactNumber: String,  mName: String, mNumber: String) {
        val newSafeContact = SafeContact(contactName, contactNumber)

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
            }

            .addOnFailureListener { exception ->
                Toast.makeText(
                    requireContext(),
                    "${exception.message}",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
    }

    @SuppressLint("StringFormatInvalid")
    private fun renderContact(uri: Uri) {
        val name = getNameByUri(uri, requireActivity().contentResolver)
        val phone = getPhoneByUri(uri, requireActivity().contentResolver)
        if (phone != null && name != null) {

            val form = listOf(mMessage, name, phone)
            if (!form.contains("")) {
                if(mName != null && mNumber != null){
                    sendRequest(name, phone, mName ?: "", mNumber ?: "")
                }
            }
        } else {
            snackBarMaker(
                requireContext(),
                requireView(),
                R.string.add_safe_contacts_manually_hint,
                Snackbar.LENGTH_INDEFINITE,
                listOf(name)
            )
                .setAction(resources.getString(R.string.permission_configuration_hint)) {}
                .show()
        }
    }

    private fun updateContactList() {
        val preferences = setSharedPreferences(requireActivity())
        val preferencesEditor = preferences.edit()
        preferencesEditor.putString(
            getString(R.string.shared_preferences_safe_contact_list),
            Gson().toJson(safeContactsList)
        )
        preferencesEditor.apply()
    }

    private fun sendNotification(notification: PushNotification, newSafeContact: SafeContact) =
        CoroutineScope(Dispatchers.IO).launch {
            try {
                print(notification.to)

                val response = RetrofitInstance.api.postNotification(notification)
                if (response.isSuccessful) {

                    requireActivity().runOnUiThread {
                        saveContacts(newSafeContact) { saveContactResponse ->
                            if (saveContactResponse) Toast.makeText(
                                requireContext(),
                                "${newSafeContact.name} será invitado a tus contactos de confianza",
                                Toast.LENGTH_SHORT
                            ).show()

                            addSafeContact(
                                Contacts(
                                    R.drawable.unknown,
                                    newSafeContact.name,
                                    formatTelephone(newSafeContact.telephone)
                                )
                            )
                        }
                    }
                } else {
                    Log.e(TAG, response.errorBody().toString())
                }
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
            }
        }

    private fun setMessage(
        mMessage: String,
        callback: ((Boolean) -> Unit)
    ) {
        if (listOf(mMessage).contains("")) callback(false)

        userId?.let { userIdentifier ->
            usersCollection
                .document(userIdentifier)
                .update("message", mMessage)
                .addOnCompleteListener {  response -> callback(response.isSuccessful) }
        }
    }

    private fun getUserInfo(){
        usersCollection
            .whereEqualTo("id", userId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.documents.isNotEmpty()) {
                   mNumber = querySnapshot.documents.first().get("telephone").toString()
                    mName = querySnapshot.documents.first().get("name").toString()
                }
            }


    }

    private fun saveContacts(
        newSafeContact: SafeContact,
        callback: (Boolean) -> Unit
    ) {


        usersCollection
            .whereEqualTo("id", userId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.documents.isNotEmpty()) {

                    val data = querySnapshot.documents[0].get("channel")

                    mChannel = Gson().fromJson(data.toString(), Channel::class.java)
                    val userMatchList = mChannel.members.filter { it.telephone == newSafeContact.telephone }

                    if(userMatchList.isEmpty()){
                        mChannel.members.add(newSafeContact)
                        userId?.let { documentResponse ->
                            usersCollection
                                .document(documentResponse)
                                .update("channel", mChannel)
                                .addOnCompleteListener { callback(it.isSuccessful) }
                        }
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


    @SuppressLint("CommitPrefEdits")
    private fun isAValidUser(): Boolean {
        val preferences = setSharedPreferences(requireActivity())
        val userMessageString =
            preferences.getString(
                resources.getString(R.string.shared_preferences_user_message),
                null
            )

        mMessage = userMessageString ?: ""
        return mMessage != ""
    }
}


