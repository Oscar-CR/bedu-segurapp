package org.bedu.segurapp

import android.app.Application
import org.bedu.segurapp.models.Preferences
import org.bedu.segurapp.models.local.ContactDb
import org.bedu.segurapp.models.local.ContactRepository

class UserLogin : Application() {
    companion object{
        lateinit var pref: Preferences
    }

    val contactRepository: ContactRepository
        get() = ContactRepository(
            ContactDb.getInstance(this)!!.contactDao()
        )

    override fun onCreate() {
        super.onCreate()
        pref = Preferences(applicationContext)
    }


}