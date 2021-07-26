package org.bedu.segurapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import org.bedu.segurapp.ui.RegisterActivity
import org.bedu.segurapp.ui.getStarted.GetStartedActivity

class MainActivity : AppCompatActivity() {
    private lateinit var btCreate: Button
    private lateinit var btnSubmit: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initComponents()

        btCreate.setOnClickListener {
            intent = Intent(applicationContext, RegisterActivity::class.java)
            startActivity(intent)
        }

        btnSubmit.setOnClickListener{
            intent = Intent(applicationContext, GetStartedActivity::class.java)
            startActivity(intent)
        }
    }


    private fun initComponents(){
        btnSubmit = findViewById(R.id.btnSubmit)
        btCreate=findViewById(R.id.btCreate)
    }
}