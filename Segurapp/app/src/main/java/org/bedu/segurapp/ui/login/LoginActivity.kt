package org.bedu.segurapp.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import org.bedu.segurapp.R
import org.bedu.segurapp.databinding.ActivityLoginBinding
import org.bedu.segurapp.helpers.makeFormValidations
import org.bedu.segurapp.UserLogin.Companion.pref
import org.bedu.segurapp.helpers.setSharedPreferences
import org.bedu.segurapp.models.Channel
import org.bedu.segurapp.models.SubscribedTo
import org.bedu.segurapp.ui.forgotPassword.BottomSheetForgotPassword
import org.bedu.segurapp.ui.home.HomeActivity
import org.bedu.segurapp.ui.register.RegisterActivity
import org.bedu.segurapp.ui.request.RequestActivity
import org.bedu.segurapp.ui.socialSecurity.SocialSecurityActivity
import java.util.*

class LoginActivity : AppCompatActivity() {

    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private lateinit var mChannel: Channel
    private val mAuth = Firebase.auth
    private val db = Firebase.firestore
    private var pushNotificationToken: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        pushNotificationListener()
        //Valida si hay usuarios en el SharedPreferences
        checkUserValues()
    }

    // Shared Preferences
    private fun checkUserValues(){
        if (pref.getName().isNotEmpty()){
            goToDetail()
        }else{
            btLoginListener()
            btCreateListener()
            btForgotPasswordListener()
            btUnknownUserListener()
        }
    }

    private fun accessToDetail(){


        if(makeFormValidations(arrayOf(binding.etLoginEmail,binding.etLoginPassword), this)){

            binding.scrollView.visibility = View.VISIBLE

            mAuth.signInWithEmailAndPassword(binding.etLoginEmail.text.toString(), binding.etLoginPassword.text.toString()).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    validateUser("login")
                } else {
                    binding.scrollView.visibility = View.INVISIBLE

                    Toast.makeText(baseContext, "Credenciales incorrectas", Toast.LENGTH_SHORT)
                        .show()
                }
            }


        }
    }

    private fun goToDetail(){
        val i = Intent(this, HomeActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }

    private fun getToken() {
        val preferences = setSharedPreferences(this)
        val tokenString = preferences.getString(getString(R.string.shared_preferences_firebase_id), null)

        if (tokenString != null) {
            pushNotificationToken = tokenString
        }
    }

    private fun validateUser(origin: String) {
        if (mAuth.currentUser != null) {
            updateToken(origin){ isTokenUpdated ->
                if(isTokenUpdated){
                    pref.saveName(binding.etLoginEmail.text.toString())
                    val mIntent = Intent(this, HomeActivity::class.java)
                    navigateToActivity(mIntent, origin)

                    //goToDetail()

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

    private fun btForgotPasswordListener() {
        binding.tvForgotPassword.setOnClickListener {
            BottomSheetForgotPassword().apply {
                show(supportFragmentManager, tag)
            }
        }
    }

    private fun btUnknownUserListener(){
        binding.tvUknownUser.setOnClickListener{
            startActivity(Intent(this, SocialSecurityActivity::class.java))
        }
    }


    private fun btLoginListener() {
        binding.btSubmit.setOnClickListener {
            accessToDetail()
        }
    }


    private fun btCreateListener() {

        binding.btCreate.setOnClickListener {
            intent = Intent(applicationContext, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun pushNotificationListener() {
        val request = intent.getStringExtra("url")
        request?.let {
            val intent = Intent(this, RequestActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("url", request)
            startActivity(intent)
        }
    }

    private fun saveUserInStored(userId: String){
        val preferences = setSharedPreferences(this)
        val preferencesEditor = preferences.edit()
        preferencesEditor?.putString(getString(R.string.shared_preferences_user_id), userId)
        preferencesEditor?.apply()
    }


    private fun updateToken(origin: String = "signIn", callback: ((Boolean) -> Unit)
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
}