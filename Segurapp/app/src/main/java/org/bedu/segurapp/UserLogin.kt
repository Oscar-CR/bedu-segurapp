package org.bedu.segurapp

import android.app.Application
import org.bedu.segurapp.UserLogin.Companion.pref
import org.bedu.segurapp.models.Preferences
import org.bedu.segurapp.models.local.ContactDb
import org.bedu.segurapp.models.local.ContactRepository
import org.bedu.segurapp.models.local.PersonRepository
import org.bedu.segurapp.models.local.data.PersonDB

class UserLogin : Application() {
    companion object{
        lateinit var pref: Preferences
    }

    val contactRepository: ContactRepository
        get() = ContactRepository(
            ContactDb.getInstance(this)!!.contactDao()
        )


    val personRepository: PersonRepository
        get() = PersonRepository(
            PersonDB.getInstance(this)!!.personDao()
        )


    override fun onCreate() {
        super.onCreate()

        pref = Preferences(applicationContext)


    }


}