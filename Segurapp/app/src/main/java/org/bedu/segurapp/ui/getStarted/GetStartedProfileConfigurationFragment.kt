package org.bedu.segurapp.ui.getStarted

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.viewpager2.widget.ViewPager2
import kotlinx.android.synthetic.main.fragment_get_started_profile_configuration.*
import org.bedu.segurapp.R
import org.bedu.segurapp.helpers.makeValidations
import org.bedu.segurapp.helpers.moveNext


class GetStartedProfileConfigurationFragment : Fragment() {
    private lateinit var btnNext: Button
    private lateinit var txtNumber: EditText
    private lateinit var txtMessageEmergency: EditText

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
        txtNumber= view.findViewById(R.id.txt_number)
        txtMessageEmergency = view.findViewById(R.id.txt_message_emergency)
    }

    private fun btnNextClickListener() {
        btnNext.setOnClickListener {

            if (makeValidations(arrayOf(txtNumber, txtMessageEmergency), requireContext())) {
                val mPager = (activity as GetStartedActivity).findViewById<ViewPager2>(R.id.pager)
                if (mPager != null) moveNext(mPager)
            }

        }
    }
}

