package org.bedu.segurapp.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import org.bedu.segurapp.R
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

        btSubmit.setOnClickListener {
            intent = Intent(applicationContext,HomeActivity::class.java)
            startActivity(intent)
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
}