package org.bedu.segurapp.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import org.bedu.segurapp.R
import org.bedu.segurapp.ui.register.RegisterActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var btCreate: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initComponents()

        btCreate.setOnClickListener {
            intent = Intent(applicationContext, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initComponents(){
        btCreate=findViewById(R.id.btCreate)
    }
}