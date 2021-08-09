package org.bedu.segurapp.ui.getStarted

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import org.bedu.segurapp.R
import org.bedu.segurapp.helpers.moveNext


class GetStartedSafeContactsFragment : Fragment() {

    private lateinit var btnNext : Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_get_started_safe_contacts, container, false)
        initComponents(view)
        btnContinueClickListener()
        return view
    }

    private fun initComponents(view: View){
        btnNext = view.findViewById(R.id.btn_next)
    }

    private fun btnContinueClickListener(){
        moveNext(requireActivity() as GetStartedActivity)
    }

}