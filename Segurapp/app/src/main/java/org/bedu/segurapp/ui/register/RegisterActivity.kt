package org.bedu.segurapp.ui.register

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.gson.Gson
import org.bedu.segurapp.R
import org.bedu.segurapp.databinding.ActivityRegisterBinding
import org.bedu.segurapp.helpers.makeFormValidations
import org.bedu.segurapp.helpers.setSharedPreferences
import org.bedu.segurapp.models.User
import org.bedu.segurapp.ui.getStarted.GetStartedActivity

class RegisterActivity : AppCompatActivity() {

    private val binding by lazy { ActivityRegisterBinding.inflate(layoutInflater) }
    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        registerProcess()
    }

    private fun registerProcess() {
       binding.btnSubmit.setOnClickListener {
            if (makeFormValidations(arrayOf(binding.etRegisterUser, binding.etRegisterTelephone, binding.etRegisterEmail, binding.etRegisterPassword),
                    applicationContext)
            ) {
                val currentUser = User(binding.etRegisterUser.text.toString(),
                    binding.etRegisterTelephone.text.toString(),
                    binding.etRegisterEmail.text.toString(),
                    binding.etRegisterPassword.text.toString())
                if (saveUserInSharedPreferences(currentUser)) {
                    val intent = Intent(this, GetStartedActivity::class.java)
                    startActivity(intent)
                    this.finish()
                }

            }
        }
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
}