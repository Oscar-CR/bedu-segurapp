package org.bedu.segurapp.ui.forgotPassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import org.bedu.segurapp.R
import org.bedu.segurapp.helpers.makeFormValidations

class BottomSheetForgotPassword : BottomSheetDialogFragment() {

    private lateinit var metEmail: TextInputEditText
    private lateinit var mbtnSend: MaterialButton

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
        metEmail = view.findViewById(R.id.met_email)
        mbtnSend = view.findViewById(R.id.mbtn_send)

        mbtnSend.setOnClickListener {
            if(makeFormValidations(arrayOf(metEmail), requireContext())){
                Toast.makeText(requireContext(), "Correo enviado correctamente", Toast.LENGTH_SHORT).show()
                this.dismiss()
            }
        }
    }
}