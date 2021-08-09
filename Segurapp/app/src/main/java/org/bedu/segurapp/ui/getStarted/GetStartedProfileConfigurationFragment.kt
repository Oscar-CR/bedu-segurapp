package org.bedu.segurapp.ui.getStarted

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.viewpager2.widget.ViewPager2
import org.bedu.segurapp.R
import org.bedu.segurapp.helpers.moveNext


class GetStartedProfileConfigurationFragment : Fragment() {
    private lateinit var btnNext: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_get_started_profile_configuration, container, false)
        initComponents(view)
        btnNextClickListener()
        return view
    }

    private fun initComponents(view: View){
        btnNext = view.findViewById(R.id.btn_next)
    }

    private fun btnNextClickListener() {
        btnNext.setOnClickListener {
            val mPager = (activity as GetStartedActivity).findViewById<ViewPager2>(R.id.pager)
            if (mPager != null) moveNext(mPager)
        }
    }
}

