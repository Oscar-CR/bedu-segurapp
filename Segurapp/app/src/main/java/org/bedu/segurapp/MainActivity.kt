package org.bedu.segurapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.bedu.segurapp.helpers.userIsLogged
import org.bedu.segurapp.ui.home.HomeActivity
import org.bedu.segurapp.ui.login.LoginActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.splash_theme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val intent = if (userIsLogged()) Intent(this, HomeActivity::class.java)
        else Intent(this, LoginActivity::class.java)
        startActivity(intent)
        this.finish()
    }
}