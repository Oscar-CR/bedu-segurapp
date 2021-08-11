package org.bedu.segurapp.ui.getStarted

import android.Manifest
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.araujo.jordan.excuseme.ExcuseMe
import com.google.android.material.snackbar.Snackbar
import org.bedu.segurapp.helpers.movePrevious
import org.bedu.segurapp.R
import org.bedu.segurapp.adapters.ViewPagerAdapter

class GetStartedActivity : AppCompatActivity() {
    private lateinit var mPager: ViewPager2
    private val mAdapter by lazy { ViewPagerAdapter(this) }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_started)
        mPager = findViewById(R.id.pager)
        mPager.adapter = mAdapter
        requestPermissions()
    }

    override fun onBackPressed() {
        if (mPager.currentItem != 0) movePrevious(mPager)
        else finish()
    }

    private fun requestPermissions() {
        ExcuseMe.couldYouGive(this).permissionFor(Manifest.permission.CAMERA,
            Manifest.permission.READ_CONTACTS, Manifest.permission.CALL_PHONE, Manifest.permission.LOCATION_HARDWARE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
        ) {
            val mSnackBarMessage =
                if (it.denied.size > 0) getString(R.string.denied_permissions_message_hint) else getString(R.string.granted_permissions_message_hint)
            val mSnackBar = Snackbar.make(findViewById(android.R.id.content), mSnackBarMessage,
                Snackbar.LENGTH_LONG)
            mSnackBar.show()
        }
    }
}