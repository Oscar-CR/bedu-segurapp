package org.bedu.segurapp.ui

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.mapbox.android.core.location.*
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import org.bedu.segurapp.R
import org.bedu.segurapp.databinding.ActivityTrackingBinding
import org.bedu.segurapp.helpers.alertDialogMaker
import org.bedu.segurapp.helpers.copyToClipboard
import org.bedu.segurapp.helpers.snackBarMaker
import org.bedu.segurapp.models.LocationHistory


class TrackingActivity : AppCompatActivity(), OnMapReadyCallback, PermissionsListener {

    private val binding by lazy {ActivityTrackingBinding.inflate(layoutInflater) }
    private var status : Boolean = false
    private var isNewTracking : Boolean = false
    private var userRegisterId = ""
    private lateinit var permissionsManager: PermissionsManager
    private lateinit var locationEngine: LocationEngine
    private lateinit var map: MapboxMap
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token) )

        userRegisterId = intent.getStringExtra("userRegisterId") ?: ""

        if(userRegisterId != ""){
            with(binding){
                setContentView(binding.root)
                mapView.onCreate(savedInstanceState)
                mapView.getMapAsync(this@TrackingActivity)
                sendHelp()
            }
        }else{
            alertDialogMaker(this, R.string.tracking_alert_title, R.string.tracking_alert_description, false)
                .setPositiveButton(getString(R.string.accept)) { _: DialogInterface, _: Int ->
                   finish()
                }
                .show()
        }


    }



    override fun onMapReady(mapboxMap: MapboxMap) {
        map = mapboxMap
        mapboxMap.setStyle(Style.MAPBOX_STREETS) {
            status = true
            enableLocationComponent(it)
        }

    }

    private fun sendHelp(){
        binding.btnHelp.setOnClickListener {
            val permissionCheck =
                ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)

            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.CALL_PHONE),
                    123
                )
            } else {
                startActivity(
                    Intent(Intent.ACTION_DIAL)
                        .setData(Uri.parse("tel:911"))
                )
            }
        }
    }

    private fun enableLocationComponent(loadedMapStyle: Style) {
        if (PermissionsManager.areLocationPermissionsGranted(Mapbox.getApplicationContext())) {
            val locationComponentActivationOptions = LocationComponentActivationOptions.builder(
                Mapbox.getApplicationContext(), loadedMapStyle)
                .useDefaultLocationEngine(false)
                .build()
            map.locationComponent.apply {
                activateLocationComponent(locationComponentActivationOptions)
                if (ActivityCompat.checkSelfPermission(Mapbox.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        Mapbox.getApplicationContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {

                    return
                }
                isLocationComponentEnabled = true
                cameraMode = CameraMode.TRACKING
                renderMode = RenderMode.COMPASS
            }
            initLocationEngine()
        } else {
            initLocationEngine()
            permissionsManager = PermissionsManager(this)
            permissionsManager.requestLocationPermissions(this@TrackingActivity )
        }
    }

    private fun initLocationEngine() {

        locationEngine = LocationEngineProvider.getBestLocationEngine(Mapbox.getApplicationContext())

        if (ActivityCompat.checkSelfPermission(Mapbox.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                Mapbox.getApplicationContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        getLocations()

    }

    override fun onExplanationNeeded(permissionsToExplain: MutableList<String>?) {}

    override fun onPermissionResult(granted: Boolean) {
        if (granted) {
            map.getStyle {
                enableLocationComponent(it)
            }
        } else {
            permissionsManager = PermissionsManager(this)
            permissionsManager.requestLocationPermissions(this@TrackingActivity)
        }
    }

    private fun getLocations(){
        val docRef = db.collection("locations").document(userRegisterId)
        docRef.addSnapshotListener(object : EventListener<DocumentSnapshot?> {
            override fun onEvent(value: DocumentSnapshot?, error: FirebaseFirestoreException?) {
                if (error != null) {
                    Log.w("TrackingActivity", "Listen failed.", error)
                    return
                }else{
                    try {
                        if(isNewTracking) clearMap()
                        isNewTracking = false
                        val coordinatesInfoHash = value?.get("coordinatesInfo") as ArrayList<HashMap<String,Any>>
                        if(coordinatesInfoHash.isNotEmpty()){
                            val coordinatesInfo = (coordinatesInfoHash).filter {it.containsValue(true) }
                            if(coordinatesInfo.isEmpty()){
                                showDialog()
                                return
                            }

                            val mJson = Gson().toJson(coordinatesInfo.first()["locationHistory"])


                            val coordinatesList = ArrayList(Gson().fromJson(
                                mJson,
                                Array<LocationHistory>::class.java).toList())

                            val mCoordinate = coordinatesList.last()
                            val userLocation = LatLng(mCoordinate.geoPoint.latitude, mCoordinate.geoPoint.longitude)

                            val position = CameraPosition.Builder()
                                .target(userLocation)
                                .zoom(18.0)
                                .tilt(12.0)
                                .build()

                            map.animateCamera(CameraUpdateFactory.newCameraPosition(position))
                            map.addMarker(
                                MarkerOptions()
                                    .position(userLocation)
                                    .title("Última ubicación ${userLocation.latitude} ; ${userLocation.longitude}")
                            )
                        }
                    }catch (ex: Exception){
                        Log.d("TrackingActivity", ex.message.toString())
                    }

                }
            }
        })
    }


    private fun showDialog(){

        alertDialogMaker(this, R.string.tracking_alert_title, R.string.tracking_alert_description, false)
            .setPositiveButton(getString(R.string.accept)) { _: DialogInterface, _: Int ->
                showAllMarkers()
            }

            .setNegativeButton(getString(R.string.refuse)) { _: DialogInterface, _: Int ->
                clearMap()
            }

            .show()


    }


    private fun showAllMarkers() {
        if (map.markers.isEmpty()) return

        snackBarMaker(this, findViewById(android.R.id.content), R.string.tracking_snack_bar_clipboard, Snackbar.LENGTH_INDEFINITE)
            .setAction(R.string.accept_hint) {
                var markerDetails = ""
                map.markers.forEachIndexed { index, marker ->
                    markerDetails += "${index + 1}. Lat: ${marker.position.latitude}  Lng: ${marker.position.longitude} \n"
                }
                this.copyToClipboard(markerDetails)
                isNewTracking = true
            }.show()
    }


    private fun clearMap(){
        map.clear()
    }

}