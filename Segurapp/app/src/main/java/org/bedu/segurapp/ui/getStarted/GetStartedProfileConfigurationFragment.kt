package org.bedu.segurapp.ui.getStarted

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import androidx.viewpager2.widget.ViewPager2
import com.araujo.jordan.excuseme.ExcuseMe
import com.google.android.material.snackbar.Snackbar
import de.hdodenhof.circleimageview.CircleImageView
import org.bedu.segurapp.R
import org.bedu.segurapp.helpers.makeFormValidations
import org.bedu.segurapp.helpers.moveNext
import org.bedu.segurapp.helpers.openAppPermissionsScreen
import org.bedu.segurapp.helpers.setSharedPreferences

class GetStartedProfileConfigurationFragment : Fragment() {
    private lateinit var btnNext: Button
    private lateinit var txtMessageEmergency: EditText
    private lateinit var civProfileImage: CircleImageView
    private lateinit var civAddImage: CircleImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view =
            inflater.inflate(R.layout.fragment_get_started_profile_configuration, container, false)
        initComponents(view)
        civAddImageClickListener()
        btnNextClickListener()
        return view
    }

    override fun onPause() {
        saveMessageInSharedPreferences(txtMessageEmergency.text.toString())
        super.onPause()
    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) setImageBitmap(result.data)
        }


    @SuppressLint("CommitPrefEdits")
    private fun saveMessageInSharedPreferences(alertMessage: String): Boolean {

        return if (alertMessage != "") {
            val preferences = setSharedPreferences(requireActivity())
            val preferencesEditor = preferences.edit()
            preferencesEditor.putString(
                getString(R.string.shared_preferences_user_message),
                alertMessage
            )
            preferencesEditor.apply()
            true
        } else false

    }

    private fun initComponents(view: View) {
        btnNext = view.findViewById(R.id.btn_next)
        txtMessageEmergency = view.findViewById(R.id.txt_message_emergency)
        civProfileImage = view.findViewById(R.id.profile_image)
        civAddImage = view.findViewById(R.id.add_image)
    }

    private fun civAddImageClickListener() {

        civAddImage.setOnClickListener {

            ExcuseMe.couldYouGive(this).permissionFor(
                android.Manifest.permission.CAMERA,
            ) {
                if (it.granted.contains(android.Manifest.permission.CAMERA)) {
                    openCamera()
                } else {

                    val snackBar = Snackbar
                        .make(
                            requireView(),
                            getString(R.string.denied_camera_permission_message_hint),
                            Snackbar.LENGTH_INDEFINITE
                        )
                        .setAction(getString(R.string.permission_configuration_hint)) {
                            openAppPermissionsScreen(requireActivity())
                        }

                    snackBar.show()
                }
            }
        }
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        resultLauncher.launch(intent)
    }

    private fun setImageBitmap(result: Intent?) {
        val extras: Bundle? = result?.extras
        val mImageBitmap = extras?.get("data") as Bitmap?
        civProfileImage.setImageBitmap(mImageBitmap)
    }

    private fun btnNextClickListener() {
        btnNext.setOnClickListener {
            if (makeFormValidations(arrayOf(txtMessageEmergency), requireContext())) {
                if (saveMessageInSharedPreferences(txtMessageEmergency.text.toString())) {
                    val mPager =
                        (activity as GetStartedActivity).findViewById<ViewPager2>(R.id.pager)
                    if (mPager != null) moveNext(mPager)
                }
            }
        }
    }
}

