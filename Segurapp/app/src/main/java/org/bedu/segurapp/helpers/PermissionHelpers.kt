package org.bedu.segurapp.helpers

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.fragment.app.FragmentActivity
import org.bedu.segurapp.R

fun openAppPermissionsScreen(activity: FragmentActivity) {
    val intent = Intent()
    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
    val uri: Uri = Uri.fromParts("package", activity.getString(R.string.package_name), null)
    intent.data = uri
    activity.startActivity(intent)
}

fun getAppPermissions(): Array<String> = arrayOf(Manifest.permission.CAMERA,
    Manifest.permission.READ_CONTACTS,
    Manifest.permission.CALL_PHONE,
    Manifest.permission.ACCESS_COARSE_LOCATION,
    Manifest.permission.ACCESS_FINE_LOCATION)