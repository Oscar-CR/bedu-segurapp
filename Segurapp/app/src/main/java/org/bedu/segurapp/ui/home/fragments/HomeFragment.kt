package org.bedu.segurapp.ui.home.fragments

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mapbox.android.core.location.*
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.mapboxsdk.Mapbox.getApplicationContext
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.bedu.segurapp.R
import org.bedu.segurapp.models.NotificationData
import org.bedu.segurapp.models.PushNotification
import org.bedu.segurapp.networking.RetrofitInstance
import org.bedu.segurapp.ui.home.HomeActivity


class HomeFragment : Fragment(), OnMapReadyCallback, PermissionsListener {

    companion object {
        const val DEFAULT_INTERVAL_IN_MILLISECONDS = 1000L
        const val DEFAULT_MAX_WAIT_TIME = DEFAULT_INTERVAL_IN_MILLISECONDS * 2
    }

    private  lateinit var mapView: MapView
    private lateinit var map: MapboxMap
    private lateinit var permissionsManager: PermissionsManager
    private lateinit var locationEngine: LocationEngine
    private lateinit var callback: LocationChangeListeningCallback
    private lateinit var button: Button
    private lateinit var buttonStop: Button
    private lateinit var floatingActionButton: FloatingActionButton
    private lateinit var location: DialogFragment
    private val db = Firebase.firestore
    private val mAuth = Firebase.auth
    private lateinit var mHelpTitle: String
    private lateinit var mHelpDescription: String
    private lateinit var mChannelId: String
    private var userId: String? = null

    private var estatus=false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        initConponents(view)

        userId = mAuth.currentUser?.uid

        findInfoFromDB(userId.toString()){ user ->
            if(user.first != ""){
                mHelpTitle = getString(R.string.notification_help_title, user.first)
                mHelpDescription = user.second
                mChannelId = user.third
            }
        }

        buttonStop.visibility = View.GONE
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
        buttonsListeners()

        return view
    }

    private fun initConponents(view: View){
        mapView = view.findViewById(R.id.mapView)
        button = view.findViewById(R.id.button)
        buttonStop = view.findViewById(R.id.buttonStop)
        floatingActionButton = view.findViewById(R.id.buttomSearchLocation)
    }

    private fun buttonsListeners(){
        buttonStop.setOnClickListener {
            buttonStop.visibility = View.GONE
            button.visibility = View.VISIBLE
            estatus=false
        }

        button.setOnClickListener {
            button.visibility = View.GONE
            buttonStop.visibility = View.VISIBLE
            estatus=true
            requestHelp()
        }

        floatingActionButton.setOnClickListener {
            estatus=false
            locationDialogFragmentShow()
        }
    }

    override fun onMapReady(mapboxMap: MapboxMap) {
        map = mapboxMap
        callback = LocationChangeListeningCallback()
        mapboxMap.setStyle(Style.MAPBOX_STREETS) {
            enableLocationComponent(it)

        }

    }

    private fun findInfoFromDB(userId: String, callback: (Triple<String, String, String>) -> Unit){
        db.collection("users")
            .whereEqualTo("id", userId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.documents.isNotEmpty()) {
                    val name = querySnapshot.documents[0].get("name").toString()
                    val message = querySnapshot.documents[0].get("message").toString()
                    val channelId = querySnapshot.documents[0].get("channel.id").toString()
                    callback(Triple(name, message, "/topics/$channelId"))
                }
            }

            .addOnFailureListener { exception ->
                Toast.makeText(
                    getApplicationContext(),
                    "${exception.message}",
                    Toast.LENGTH_SHORT
                ).show()

                callback(Triple("", "", ""))
            }
        // mHelpTitle
    }

    private fun enableLocationComponent(loadedMapStyle: Style) {
        if (PermissionsManager.areLocationPermissionsGranted(getApplicationContext())) {
            val locationComponentActivationOptions = LocationComponentActivationOptions.builder(
                getApplicationContext(), loadedMapStyle)
                .useDefaultLocationEngine(false)
                .build()
            map.locationComponent.apply {
                activateLocationComponent(locationComponentActivationOptions)
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        getApplicationContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {

                    return
                }
                isLocationComponentEnabled = true                       // Enable to make component visible
                cameraMode = CameraMode.TRACKING                        // Set the component's camera mode
                renderMode = RenderMode.COMPASS                         // Set the component's render mode
            }
            initLocationEngine()
        } else {
            initLocationEngine()
            permissionsManager = PermissionsManager(this)
            permissionsManager.requestLocationPermissions(activity )
        }
    }


    private fun initLocationEngine() {

        locationEngine = LocationEngineProvider.getBestLocationEngine(getApplicationContext())
        val request = LocationEngineRequest
            .Builder(DEFAULT_INTERVAL_IN_MILLISECONDS)
            .setPriority(LocationEngineRequest.PRIORITY_BALANCED_POWER_ACCURACY)
            .setMaxWaitTime(DEFAULT_MAX_WAIT_TIME)
            .build()
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                getApplicationContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        locationEngine.requestLocationUpdates(request, callback, Looper.getMainLooper())
        locationEngine.getLastLocation(callback)

    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private inner class LocationChangeListeningCallback :
        LocationEngineCallback<LocationEngineResult> {

        override fun onSuccess(result: LocationEngineResult?)  {
            result?.lastLocation ?: return
            if (estatus){
                if (result.lastLocation != null){
                    val lat = result.lastLocation?.latitude!!
                    val lng = result.lastLocation?.longitude!!

                    val latLng = LatLng(lat, lng)

                    if (result.lastLocation != null) {

                        map.locationComponent.forceLocationUpdate(result.lastLocation)
                        val position = CameraPosition.Builder()
                            .target(latLng)
                            .zoom(18.0)
                            .tilt(12.0)
                            .build()
                        map.animateCamera(CameraUpdateFactory.newCameraPosition(position))
                        Toast.makeText(getApplicationContext(), "Latitud: $lat Longitud: $lng ", Toast.LENGTH_SHORT).show()

                    }
                }
            }else{
                if (result.lastLocation != null){
                    val lat = result.lastLocation?.latitude!!
                    val lng = result.lastLocation?.longitude!!

                    val latLng = LatLng(lat, lng)

                    if (result.lastLocation != null) {
                        map.locationComponent.forceLocationUpdate(result.lastLocation)
                        val position = CameraPosition.Builder()
                            .target(latLng)
                            .zoom(18.0)
                            .tilt(12.0)
                            .build()
                        map.animateCamera(CameraUpdateFactory.newCameraPosition(position))
                    }
                }
            }

        }

        override fun onFailure(exception: Exception) {
            estatus=false
        }
    }

    override fun onExplanationNeeded(permissionsToExplain: MutableList<String>?) {
        // Toast.makeText(this, "Necesita permisos para utilizar la app", Toast.LENGTH_LONG).show()
    }

    override fun onPermissionResult(granted: Boolean) {
        if (granted) {
            map.getStyle {
                enableLocationComponent(it)
            }
        } else {
            //Solicita los permisos en caso de no aceptar
            permissionsManager = PermissionsManager(this)
            permissionsManager.requestLocationPermissions(activity)
        }
    }


    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        locationEngine.removeLocationUpdates(callback)
        estatus = false
        mapView.onDestroy()

    }

    private fun alertNotification() {

        val notification = context?.let {
            NotificationCompat.Builder(it, HomeActivity.CHANNEL_HELP)
                .setSmallIcon(R.drawable.segurappalert) //seteamos el ícono de la push notification
                .setContentTitle(getString(R.string.notification_title)) //seteamos el título de la notificación
                .setContentText(getString(R.string.notification_text)) //seteamos el cuerpo de la notificación
                .setPriority(NotificationCompat.PRIORITY_DEFAULT) //Ponemos una prioridad por defecto
                .build()
        }

        context?.let {
            NotificationManagerCompat.from(it).run {
                if (notification != null) {
                    notify(20, notification)
                }
            }
        }
    }

    private fun locationDialogFragmentShow() {
        location= LocationDialogFragment()
        location.show(childFragmentManager,"s")

    }


    private fun requestHelp(){
        PushNotification(
            NotificationData(
                mHelpTitle,
                mHelpDescription,
                mChannelId,
                mChannelId
            ),
            mChannelId
        ).also { pushNotification ->
            sendNotification(pushNotification)
        }
    }
    private fun sendNotification(notification: PushNotification) =
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitInstance.api.postNotification(notification)
                if (response.isSuccessful) {

                    requireActivity().runOnUiThread {
                        alertNotification()
                    }

                } else {
                    Log.e(ContentValues.TAG, response.errorBody().toString())
                }
            } catch (e: Exception) {
                Log.e(ContentValues.TAG, e.toString())
            }
        }

}