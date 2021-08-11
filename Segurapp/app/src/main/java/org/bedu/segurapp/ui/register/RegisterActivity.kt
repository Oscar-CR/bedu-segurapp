package org.bedu.segurapp.ui.register

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.gson.Gson
import org.bedu.segurapp.R
import org.bedu.segurapp.helpers.makeFormValidations
import org.bedu.segurapp.helpers.setSharedPreferences
import org.bedu.segurapp.models.User
import org.bedu.segurapp.ui.getStarted.GetStartedActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var preferences: SharedPreferences
    private lateinit var btnSubmit: Button
    private lateinit var etRegisterUser: EditText
    private lateinit var etRegisterEmail: EditText
    private lateinit var etRegisterPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        initComponents()
        registerProcess()
    }

    private fun initComponents() {
        btnSubmit = findViewById(R.id.btnSubmit)
        etRegisterUser = findViewById(R.id.etRegisterUser)
        etRegisterEmail = findViewById(R.id.etRegisterEmail)
        etRegisterPassword = findViewById(R.id.etRegisterPassword)
    }

    private fun registerProcess() {
        btnSubmit.setOnClickListener {
            if (makeFormValidations(arrayOf(etRegisterUser, etRegisterEmail, etRegisterPassword),
                    applicationContext)
            ) {
                val currentUser = User(etRegisterUser.text.toString(),
                    etRegisterEmail.text.toString(),
                    etRegisterPassword.text.toString())
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