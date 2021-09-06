package org.bedu.segurapp.ui.home.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.araujo.jordan.excuseme.ExcuseMe
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import org.bedu.segurapp.R
import org.bedu.segurapp.databinding.FragmentHomeBinding
import org.bedu.segurapp.helpers.getAppPermissions
import org.bedu.segurapp.helpers.openAppPermissionsScreen
import org.bedu.segurapp.helpers.snackBarMaker


class HomeFragment : Fragment() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var stopService = false
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val locationManager by lazy { requireContext().getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view: View = binding.root

        requestPermissions()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        sendAlert()
        stopAlert()
        return view
    }

    // Este botón inicia con getLocation y este a su vez el listener
    private fun sendAlert() {
        with(binding)
        {
            btnHelp.setOnClickListener {
                stopService = false
                btnStop.visibility = View.VISIBLE
                btnHelp.visibility = View.GONE
                validatePermissionInRuntime()
            }
        }
    }

    // Este botón detiene listener que desata getLocation
    private fun stopAlert() {
        with(binding) {
            btnStop.setOnClickListener {
                btnStop.visibility = View.GONE
                btnHelp.visibility = View.VISIBLE
                stopService = true
            }
        }
    }


    // Valida que se tengan los permisos para continuar o manda a Settings
    private fun validatePermissionInRuntime() {
        ExcuseMe.couldYouGive(this).permissionFor(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        ) {
            if (it.granted.contains(Manifest.permission.ACCESS_FINE_LOCATION) && it.granted.contains(
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            ) {
                getLocation()
            } else {
                snackBarMaker(
                    requireContext(),
                    requireView(),
                    R.string.denied_location_message_hint,
                    Snackbar.LENGTH_INDEFINITE
                )
                    .setAction(getString(R.string.permission_configuration_hint)) {
                        openAppPermissionsScreen(requireActivity())
                    }
            }
        }
    }

    // Se piden los permisos por primera vez
    private fun requestPermissions() {
        ExcuseMe.couldYouGive(this).permissionFor(*getAppPermissions()) {
            if (it.denied.size > 0) {
                Toast.makeText(
                    requireContext(),
                    R.string.denied_permissions_message_hint,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {

        try {

            val hasGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

            if (hasGps) {

                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    2000,
                    0.5f,
                    object : LocationListener {

                        override fun onStatusChanged(
                            provider: String?,
                            status: Int,
                            extras: Bundle?
                        ) {
                            Log.d("GPS", "$status")
                        }

                        override fun onLocationChanged(mLocation: Location) {
                            Toast.makeText(
                                requireContext(),
                                "${mLocation.latitude}, ${mLocation.longitude}",
                                Toast.LENGTH_SHORT
                            ).show()

                            // Aquí se detiene el listener
                            if (stopService) {
                                locationManager.removeUpdates(this)
                            }
                        }

                        override fun onProviderEnabled(message: String) {
                            Log.d("GPS", message)
                        }

                        override fun onProviderDisabled(message: String) {
                            Log.d("GPS", message)
                        }

                        //Pd. le puse todos los otros override porque leí por ahí que eran obligatorios. onLocationChanged es el único que uso realmente
                    })


            } else {
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
        } catch (e: Exception) {
            Log.d("GPS", e.message.toString())
        }

    }

}