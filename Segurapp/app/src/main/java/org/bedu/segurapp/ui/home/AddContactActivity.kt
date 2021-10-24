package org.bedu.segurapp.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import org.bedu.segurapp.databinding.ActivityAddContactBinding
import org.bedu.segurapp.helpers.makeFormValidations

class AddContactActivity : AppCompatActivity() {

    private val binding by lazy { ActivityAddContactBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setAnimation()

        binding.btnSaveContact.setOnClickListener{
            saveContact()
        }
    }

    private fun setAnimation() {
        with(binding){
            loginAnimation.setAnimation("user.json")
            loginAnimation.playAnimation()
        }
    }

    private fun saveContact(){
        if(makeFormValidations(arrayOf(binding.etUserAdd, binding.etPhoneAdd), this)){
            Toast.makeText(this,"Contacto agregado existosamente",Toast.LENGTH_LONG).show()
        }
    }
}