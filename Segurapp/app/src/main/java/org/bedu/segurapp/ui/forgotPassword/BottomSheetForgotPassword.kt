package org.bedu.segurapp.ui.forgotPassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.bedu.segurapp.R

class BottomSheetForgotPassword : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_forgot_password, container, false)
        initComponents(view)
        return view
    }

    private fun initComponents(view: View){
    }
}