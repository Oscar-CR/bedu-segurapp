package org.bedu.segurapp.models

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class AirplaneReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        val airplaneState = intent?.let {
            if(it.getBooleanExtra("state",false)) "Activado" else "Desactivado"
        }

        Toast.makeText(context,"Modo avión $airplaneState",Toast.LENGTH_LONG).show()
    }

}