package org.bedu.segurapp

import android.annotation.SuppressLint
import android.app.Application
import org.bedu.segurapp.models.Preferences

class UserLogin : Application() {
    companion object{
        @SuppressLint("StaticFieldLeak")
        lateinit var pref: Preferences
    }

    override fun onCreate() {
        super.onCreate()
        pref = Preferences(applicationContext)
    }


}