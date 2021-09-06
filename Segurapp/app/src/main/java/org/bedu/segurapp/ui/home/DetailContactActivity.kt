package org.bedu.segurapp.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.bedu.segurapp.R
import org.bedu.segurapp.adapters.USER_NAME
import org.bedu.segurapp.adapters.USER_PHONE
import org.bedu.segurapp.databinding.ActivityDetailContactBinding

class DetailContactActivity : AppCompatActivity() {

    private val binding by lazy { ActivityDetailContactBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val bundle = intent.extras
        val name = bundle?.getString(USER_NAME)
        val phone = bundle?.getString(USER_PHONE)

        with(binding){
            tvDetailContactSaludo.text = getString(R.string.hello_message,name)
            tvDetailContactPhone.text = phone
        }
    }
}