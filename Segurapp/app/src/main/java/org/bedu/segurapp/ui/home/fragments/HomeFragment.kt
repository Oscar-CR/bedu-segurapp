package org.bedu.segurapp.ui.home.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.bedu.segurapp.R


class HomeFragment : Fragment() {
    private lateinit var btn_help: ExtendedFloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        initComponents(view)
        sendAlert()
        return view
    }

    private fun initComponents(view: View){
        btn_help = view.findViewById(R.id.btn_help)
    }

    private fun sendAlert(){
        btn_help.setOnClickListener{
            Toast.makeText(context, "Alerta enviada",Toast.LENGTH_LONG).show()
        }
    }
}