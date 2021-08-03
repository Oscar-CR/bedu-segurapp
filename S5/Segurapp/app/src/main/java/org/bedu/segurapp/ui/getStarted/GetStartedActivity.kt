package org.bedu.segurapp.ui.getStarted

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.MotionEvent
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import org.bedu.segurapp.R


private const val DRAWABLE_RIGHT = 2

class GetStartedActivity : AppCompatActivity() {

    private lateinit var btnProfileImage: de.hdodenhof.circleimageview.CircleImageView
    private lateinit var btnAddImage: de.hdodenhof.circleimageview.CircleImageView
    private lateinit var txtLocation: EditText
    private lateinit var txtNumber: EditText
    private lateinit var btnSave: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_started)
        initComponents()
        setOnProfileClickListener()
        setTouchDrawable(txtLocation)
        setButtonOnClickListener()
    }

    private fun initComponents() {
        btnProfileImage = findViewById(R.id.profile_image)
        btnAddImage = findViewById(R.id.add_image)
        txtLocation = findViewById(R.id.txt_location)
        txtNumber = findViewById(R.id.txt_number)
        btnSave = findViewById(R.id.btn_save)
    }

    private fun setOnProfileClickListener() {
        btnAddImage.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            resultLauncher.launch(intent)
        }
    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) setImageBitmap(result.data)
        }

    private fun setImageBitmap(result: Intent?) {
        val extras: Bundle? = result?.extras
        val mImageBitmap = extras?.get("data") as Bitmap?
        btnProfileImage.setImageBitmap(mImageBitmap)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setTouchDrawable(mnEditText: EditText) {

        mnEditText.setOnTouchListener { v, event ->
            when (event?.action) {
                MotionEvent.ACTION_UP -> {
                    if (event.rawX >= (mnEditText.right - mnEditText.compoundDrawables[DRAWABLE_RIGHT].bounds.width())) {
                        editInformation(mnEditText)
                    }
                }
            }
            v?.onTouchEvent(event) ?: true
        }
    }

    private fun editInformation(editText: EditText) {
        when (editText.id) {
            R.id.txt_location -> {
                openMapBox()
            }
        }
    }

    private fun openMapBox() {
        Toast.makeText(this, "Opening Mapbox", Toast.LENGTH_SHORT).show()
    }

    private fun setButtonOnClickListener(){
        btnSave.setOnClickListener {
            val intent = Intent(this, SafeContactsActivity::class.java)
            startActivity(intent)
        }
    }

}