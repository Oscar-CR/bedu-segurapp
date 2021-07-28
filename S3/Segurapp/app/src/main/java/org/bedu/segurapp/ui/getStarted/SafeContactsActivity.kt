package org.bedu.segurapp.ui.getStarted

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.DialogOnDeniedPermissionListener
import com.karumi.dexter.listener.single.PermissionListener
import org.bedu.segurapp.R
import org.bedu.segurapp.helpers.getNameByUri
import org.bedu.segurapp.helpers.getPhoneByUri


class SafeContactsActivity : AppCompatActivity() {

    private lateinit var btnSearchContacts: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_safe_contacts)
        initComponents()
        btnSearchContacts.setOnClickListener { permissionChecker() }
    }

    private fun initComponents() {
        btnSearchContacts = findViewById(R.id.btn_search_contacts)
    }

    private fun permissionChecker() {
        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_CONTACTS)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                        openContactsList()
                    }

                    @SuppressLint("StringFormatInvalid")
                    override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                        contactDeniedPermissionListener(getString(R.string.read_contact_permission_title),
                                getString(R.string.read_contact_permission_message), getString(R.string.ok))
                    }

                    override fun onPermissionRationaleShouldBeShown(p0: PermissionRequest?, p1: PermissionToken?) {
                        p1?.continuePermissionRequest()
                    }

                }).check()
    }

    private fun contactDeniedPermissionListener(title: String, message: String, buttonText: String) {
        DialogOnDeniedPermissionListener.Builder
                .withContext(this)
                .withTitle(title)
                .withMessage(message)
                .withButtonText(buttonText)
                .build()
    }

    private fun openContactsList() {
        val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
        resultLauncher.launch(intent)
    }

    private fun renderContact(uri: Uri) {
        val name = getNameByUri(uri, contentResolver)
        val phone = getPhoneByUri(uri, contentResolver)
        Toast.makeText(this, "$name: $phone", Toast.LENGTH_SHORT).show()
    }

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intentResult: Intent? = result.data
            intentResult?.data?.let { renderContact(it) }
        }
    }

}