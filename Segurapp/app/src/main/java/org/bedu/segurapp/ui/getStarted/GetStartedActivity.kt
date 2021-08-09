package org.bedu.segurapp.ui.getStarted

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import org.bedu.segurapp.R
import org.bedu.segurapp.adapters.ViewPagerAdapter
import org.bedu.segurapp.helpers.movePrevious

class GetStartedActivity : AppCompatActivity() {
    private lateinit var mPager: ViewPager2
    private val adapter by lazy { ViewPagerAdapter(this)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_started)
        mPager = findViewById(R.id.pager)
        mPager.adapter = adapter
    }

    override fun onBackPressed() {
        if(mPager.currentItem != 0) movePrevious(mPager)
        else finish()
    }
}
