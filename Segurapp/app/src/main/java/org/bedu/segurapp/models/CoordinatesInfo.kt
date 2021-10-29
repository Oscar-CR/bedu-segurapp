package org.bedu.segurapp.models

import com.google.firebase.Timestamp

data class CoordinatesInfo (var id: String = "", var active: Boolean = true, var info: Timestamp = Timestamp.now(), var locationHistory: ArrayList<LocationHistory> = ArrayList())