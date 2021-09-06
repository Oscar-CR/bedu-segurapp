package org.bedu.segurapp.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.bedu.segurapp.databinding.ActivityLoginBinding
import org.bedu.segurapp.helpers.makeFormValidations
import org.bedu.segurapp.models.UserLogin.Companion.pref
import org.bedu.segurapp.ui.forgotPassword.BottomSheetForgotPassword
import org.bedu.segurapp.ui.home.HomeActivity
import org.bedu.segurapp.ui.register.RegisterActivity

class LoginActivity : AppCompatActivity() {

    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //Valida si hay usuarios en el SharedPreferences
        checkUserValues()

        binding.btSubmit.setOnClickListener {
            accessToDetail()
        }

        binding.btCreate.setOnClickListener {
            intent = Intent(applicationContext, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.tvForgotPassword.setOnClickListener {
            BottomSheetForgotPassword().apply {
                show(supportFragmentManager, tag)
            }
        }
    }

    // Shared Preferences
    private fun checkUserValues(){
        if (pref.getName().isNotEmpty()){
            goToDetail()
        }
    }

    private fun accessToDetail(){

        if(makeFormValidations(arrayOf(binding.etLoginEmail,binding.etLoginPassword), this)){
            pref.saveName(binding.etLoginEmail.text.toString())
            goToDetail()
        }
    }

    private fun goToDetail(){
        val i = Intent(this, HomeActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }
}