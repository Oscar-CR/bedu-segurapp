package org.bedu.segurapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import org.bedu.segurapp.R

class MyInfoActivity : AppCompatActivity() {

    lateinit var miName: EditText
    lateinit var miLastName: EditText
    lateinit var miEmail: EditText
    lateinit var miContact: EditText
    lateinit var btnBack:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_myinfo)
        viewInitializations()

        backButtom()
    }


    private fun viewInitializations() {

        miName = findViewById(R.id.mi_first_name)
        miLastName = findViewById(R.id.mi_last_name)
        miEmail  = findViewById(R.id.mi_email)
        miContact = findViewById(R.id.mi_contact)
        btnBack = findViewById(R.id.btn_back)


        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    private fun validateInput(): Boolean {
        if (miName.text.toString() == "") {
            miName.error = "Por favor escribe tu nombre"
            return false
        }
        if (miLastName.text.toString() == "") {
            miLastName.error = "Por favor escribe tu apellido"
            return false
        }
        if (miEmail.text.toString() == "") {
            miEmail.error = "Por favor escribe tu email"
            return false
        }

        if (miContact.text.toString() == "") {
            miContact.error = "Por favor escribe tu número"
            return false
        }


        if (!isEmailValid(miEmail.text.toString())) {
            miEmail.error = "Escribe un email válido"
            return false
        }

        return true
    }

    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }


    fun performEditProfile (view: View) {
        if (validateInput()) {

            miName.text.toString()
            miLastName.text.toString()
            miEmail.text.toString()
            miContact.text.toString()

            Toast.makeText(this,"Has actualizado tu perfil", Toast.LENGTH_SHORT).show()

        }
    }

    private fun backButtom(){
        btnBack.setOnClickListener {
            val i = Intent(this, HomeActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(i)
        }
    }


}