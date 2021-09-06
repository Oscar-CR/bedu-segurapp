package org.bedu.segurapp.models

import android.content.Context



class Preferences(val context : Context) {

    private val storage by lazy{ context.getSharedPreferences(SHARED_NAME,0)}

    companion object{
        private const val SHARED_NAME = "MyName"
        const val SHARED_USER_NAME ="Username"
    }

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