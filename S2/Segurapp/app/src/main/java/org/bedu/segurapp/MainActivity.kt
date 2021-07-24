package org.bedu.segurapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    private lateinit var btCreate: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btCreate=findViewById(R.id.btCreate)

        btCreate.setOnClickListener {

            intent = Intent(applicationContext, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}