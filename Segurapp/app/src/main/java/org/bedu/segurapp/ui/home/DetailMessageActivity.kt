package org.bedu.segurapp.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.bedu.segurapp.R
import org.bedu.segurapp.adapters.MESSAGE_NAME
import org.bedu.segurapp.adapters.MESSAGE_TEXT
import org.bedu.segurapp.databinding.ActivityDetailMessageBinding

class DetailMessageActivity : AppCompatActivity() {

    private val binding by lazy{ActivityDetailMessageBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_message)

        val bundle = intent.extras
        val name = bundle?.getString(MESSAGE_NAME)
        val text = bundle?.getString(MESSAGE_TEXT)

        with(binding){
            tvDetailMessageUser.text =  name
            tvDetailMessageText.text = text
        }
    }
}