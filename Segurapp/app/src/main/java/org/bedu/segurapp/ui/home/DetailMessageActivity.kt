package org.bedu.segurapp.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import org.bedu.segurapp.R
import org.bedu.segurapp.adapters.MESSAGE_NAME
import org.bedu.segurapp.adapters.MESSAGE_TEXT
import org.w3c.dom.Text

class DetailMessageActivity : AppCompatActivity() {

    private lateinit var tvDetailMessageUser: TextView
    private lateinit var tvDetailMessageText: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_message)

        tvDetailMessageUser =  findViewById(R.id.tvDetailMessageUser)
        tvDetailMessageText =  findViewById(R.id.tvDetailMessageText)

        val bundle = intent.extras

        val name = bundle?.getString(MESSAGE_NAME)
        val text = bundle?.getString(MESSAGE_TEXT)

        tvDetailMessageUser.text =  name
        tvDetailMessageText.text = text

    }
}