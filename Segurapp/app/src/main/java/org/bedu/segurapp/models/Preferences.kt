package org.bedu.segurapp.models

import android.content.Context

class Preferences(val context : Context) {
    val SHARED_NAME = "MyName"
    val SHARED_USER_NAME ="Username"
    val storage = context.getSharedPreferences(SHARED_NAME,0)

    fun saveName(name:String){
        storage.edit().putString(SHARED_USER_NAME, name).apply()
    }

    fun getName():String{
        return storage.getString(SHARED_USER_NAME,"")!!
    }

    // Borra los datos almacenados
    fun wipe(){
        storage.edit().clear().apply()
    }

}