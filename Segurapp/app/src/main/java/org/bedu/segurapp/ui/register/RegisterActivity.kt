package org.bedu.segurapp.ui.register

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import org.bedu.segurapp.R
import org.bedu.segurapp.UserLogin
import org.bedu.segurapp.databinding.ActivityRegisterBinding
import org.bedu.segurapp.helpers.makeFormValidations
import org.bedu.segurapp.helpers.setSharedPreferences
import org.bedu.segurapp.models.*
import org.bedu.segurapp.ui.getStarted.GetStartedActivity
import java.util.*
import kotlin.collections.ArrayList

class RegisterActivity : AppCompatActivity() {

    private val binding by lazy { ActivityRegisterBinding.inflate(layoutInflater) }
    private val mAuth = Firebase.auth
    private lateinit var mChannel: Channel
    private lateinit var preferences: SharedPreferences
    private lateinit var mTelephone: String
    private val db = Firebase.firestore
    private var pushNotificationToken: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        registerProcess()
    }

    private fun registerProcess() {
       binding.btnSubmit.setOnClickListener {
           val email = binding.etRegisterEmail.text.toString()
           val password = binding.etRegisterPassword.text.toString()

            if (makeFormValidations(arrayOf(binding.etRegisterUser, binding.etRegisterTelephone, binding.etRegisterEmail, binding.etRegisterPassword),
                    applicationContext)
            ) {

                mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            validateUser("signIn")
                        }
                    }
            }
        }
    }

    private fun getToken() {
        val preferences = setSharedPreferences(this)
        val tokenString = preferences.getString(getString(R.string.shared_preferences_firebase_id), null)

        if (tokenString != null) {
            pushNotificationToken = tokenString
        }
    }

    private fun updateToken(origin: String = "signIn",
                            callback: ((Boolean) -> Unit)
    ) {
        getToken()

        if (origin != "signIn"){
            mAuth.currentUser?.uid?.let { userId ->
                db.collection("users")
                    .document(userId)
                    .update("pushNotificationKey",  pushNotificationToken.toString())
                    .addOnCompleteListener {
                        if(it.isSuccessful){
                            subscribedToChannelList(userId){ response ->
                                callback(response)
                            }
                        }
                    }
            }
        } else callback(true)

    }

    private fun subscribedToChannelList(filter: String, callback: ((Boolean) -> Unit)) {
        db.collection("users")
            .whereEqualTo("id", filter)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.documents.isNotEmpty()) {
                    val subscribedData = querySnapshot.documents[0].get("subscribedToList").toString()
                    val mSubscribedToList = Gson().fromJson(subscribedData, Array<SubscribedTo>::class.java)

                    mSubscribedToList.forEach {
                        FirebaseMessaging.getInstance().subscribeToTopic(it.channelId)
                    }

                    callback(true)
                }
            }
    }


    private fun validateUser(origin: String) {
        if (mAuth.currentUser != null) {
            updateToken(origin){ isTokenUpdated ->
                if(isTokenUpdated){

                    val currentUser = User(binding.etRegisterUser.text.toString(), binding.etRegisterEmail.text.toString(), "", "", binding.etRegisterTelephone.text.toString())

                    if (saveUserInSharedPreferences(currentUser)) {
                        val mIntent = Intent(this, GetStartedActivity::class.java)
                        if (saveInDB(origin)){
                            UserLogin.pref.saveName(binding.etRegisterEmail.text.toString())
                            navigateToActivity(mIntent, origin)
                        }
                    }
                }
            }
        }
    }


    private fun navigateToActivity(mIntent: Intent,origin: String) {
        mIntent.putExtra("userId", mAuth.currentUser?.uid)
        mAuth.currentUser?.uid?.let { saveUserInStored(it) }

        if(origin != "login"){
            mIntent.putExtra("channelId", mChannel.id)
        }

        startActivity(mIntent)
        finish()
    }

    private fun saveUserInStored(userId: String){
        val preferences = setSharedPreferences(this)
        val preferencesEditor = preferences.edit()
        preferencesEditor?.putString(getString(R.string.shared_preferences_user_id), userId)
        preferencesEditor?.apply()
    }


    @SuppressLint("CommitPrefEdits")
    private fun saveUserInSharedPreferences(currentUser: User): Boolean {
        preferences = setSharedPreferences(this)
        val preferencesEditor = preferences.edit()
        preferencesEditor.putString(getString(R.string.shared_preferences_current_user),
            Gson().toJson(currentUser))
        preferencesEditor.apply()
        return true
    }

    private fun saveInDB(origin: String): Boolean {
        if (origin == "login") return true



        val safeContactList: MutableList<SafeContact> = ArrayList()
        val mEmail = binding.etRegisterEmail.text.toString()
        val mPassword = binding.etRegisterPassword.text.toString()
        mTelephone = binding.etRegisterTelephone.text.toString()
        val form = listOf(mEmail, mPassword, mTelephone)




        return if (!form.contains("")) {

            val mCoordinatesInfo = ArrayList<CoordinatesInfo>()
            val mLocationContact = LocationContact(mAuth.currentUser?.uid.toString(), mCoordinatesInfo)

            mChannel = Channel(UUID.randomUUID().toString(), safeContactList)

            val newUser = NewUser(
                mAuth.currentUser?.uid.toString(),
                pushNotificationToken.toString(),
                mEmail,
                mTelephone,
                "",
                mChannel
            )

            db.collection("users").document(mAuth.currentUser?.uid.toString()).set(newUser)
            db.collection("locations").document(mAuth.currentUser?.uid.toString()).set(mLocationContact)

            true

        } else false
    }

}