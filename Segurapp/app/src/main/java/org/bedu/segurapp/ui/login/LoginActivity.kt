package org.bedu.segurapp.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Button
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_login.*
import org.bedu.segurapp.R
import org.bedu.segurapp.models.UserLogin.Companion.pref
import org.bedu.segurapp.ui.forgotPassword.BottomSheetForgotPassword
import org.bedu.segurapp.ui.home.HomeActivity
import org.bedu.segurapp.ui.register.RegisterActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var btCreate: Button
    private lateinit var btSubmit: Button
    private lateinit var tvForgotPassword: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initComponents()

        //Valida si hay usuarios en el SharedPreferences
        checkUserValues()

        btSubmit.setOnClickListener {
            //intent = Intent(applicationContext,HomeActivity::class.java)
            //startActivity(intent)
            initUI()
        }

        btCreate.setOnClickListener {
            intent = Intent(applicationContext, RegisterActivity::class.java)
            startActivity(intent)
        }

        tvForgotPassword.setOnClickListener {
            BottomSheetForgotPassword().apply {
                show(supportFragmentManager, tag)
            }
        }
    }

    private fun initComponents(){
        btCreate = findViewById(R.id.btCreate)
        btSubmit = findViewById(R.id.btSubmit)
        tvForgotPassword = findViewById(R.id.tvForgotPassword)

    }


    // Shared Preferences
    private fun checkUserValues(){
        if (pref.getName().isNotEmpty()){
            goToDetail()
        }
    }

    private fun initUI(){
        accessToDetail()
    }

    private fun accessToDetail(){
        if (etLoginEmail.text.toString().isNotEmpty()){
            pref.saveName(etLoginEmail.text.toString())
            goToDetail()
        }else{

        }
    }

    private fun goToDetail(){
        val i = Intent(this, HomeActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }





}