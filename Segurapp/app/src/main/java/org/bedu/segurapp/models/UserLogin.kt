package org.bedu.segurapp.models

import android.app.Application
import java.util.prefs.Preferences

class UserLogin : Application() {
    companion object{
        lateinit var pref: org.bedu.segurapp.models.Preferences
    }

    override fun onCreate() {
        super.onCreate()
        pref = Preferences(applicationContext)
    }
}