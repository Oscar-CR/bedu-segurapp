package org.bedu.segurapp.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import org.bedu.segurapp.R
import org.bedu.segurapp.adapters.USER_NAME
import org.bedu.segurapp.adapters.USER_PHONE

class DetailContactActivity : AppCompatActivity() {

    private  lateinit var tvDetailContactSaludo: TextView
    private lateinit var tvDetailContactPhone : TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_contact)

        tvDetailContactSaludo = findViewById(R.id.tvDetailContactSaludo)
        tvDetailContactPhone = findViewById(R.id.tvDetailContactPhone)

        val bundle = intent.extras

        val name = bundle?.getString(USER_NAME)

        val phone = bundle?.getString(USER_PHONE)

        tvDetailContactSaludo.text = "Hola $name"
        tvDetailContactPhone.text = phone
    }
}