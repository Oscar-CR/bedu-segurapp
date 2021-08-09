package org.bedu.segurapp.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import org.bedu.segurapp.R
import org.bedu.segurapp.ui.getStarted.GetStartedActivity

class RegisterActivity : AppCompatActivity() {
    private lateinit var btSubmit: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        btSubmit = findViewById(R.id.btSubmit)
        registerProcess()
    }

    fun registerProcess(){
        btSubmit.setOnClickListener {
            val intent = Intent(this, GetStartedActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}