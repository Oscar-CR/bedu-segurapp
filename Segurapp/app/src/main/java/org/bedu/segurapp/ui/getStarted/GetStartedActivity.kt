package org.bedu.segurapp.ui.getStarted

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.araujo.jordan.excuseme.ExcuseMe
import com.google.android.material.snackbar.Snackbar
import org.bedu.segurapp.helpers.movePrevious
import org.bedu.segurapp.R
import org.bedu.segurapp.adapters.ViewPagerAdapter
import org.bedu.segurapp.helpers.getAppPermissions
import org.bedu.segurapp.helpers.snackBarMaker

class GetStartedActivity : AppCompatActivity() {

    private lateinit var mPager: ViewPager2
    private val mAdapter by lazy { ViewPagerAdapter(this) }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_started)
        initComponents()
        setViewPagerAdapter()
        requestPermissions()
    }

    override fun onBackPressed() {
        if (mPager.currentItem != 0) movePrevious(mPager)
        else finish()
    }

    private fun initComponents(){
        mPager = findViewById(R.id.pager)
    }

    private fun setViewPagerAdapter(){
        mPager.adapter = mAdapter
    }

    private fun requestPermissions() {
        ExcuseMe.couldYouGive(this).permissionFor(*getAppPermissions()) {
            val mSnackBarMessageId =
                if (it.denied.size > 0) R.string.denied_permissions_message_hint else R.string.granted_permissions_message_hint
            snackBarMaker(this,
                findViewById(android.R.id.content),
                mSnackBarMessageId,
                Snackbar.LENGTH_LONG).show()
        }
    }

}