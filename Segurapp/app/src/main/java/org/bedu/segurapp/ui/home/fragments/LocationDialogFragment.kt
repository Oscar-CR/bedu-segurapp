package org.bedu.segurapp.ui.home.fragments

import android.animation.ValueAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.airbnb.lottie.LottieAnimationView
import org.bedu.segurapp.R


class LocationDialogFragment : DialogFragment() {

    private lateinit var locationAnimation: LottieAnimationView
    private lateinit var btn_dialog_location: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_location_dialog, container, false)
        initComponents(view)
        setAnimation()
        buttomAccept()
        return  view
    }

    private fun initComponents(view: View) {
        locationAnimation = view.findViewById(R.id.location_animation)
        btn_dialog_location = view.findViewById(R.id.btn_dialog_location)
    }

    private fun setAnimation() {
        locationAnimation.setAnimation("location.json")
        locationAnimation.playAnimation()
        locationAnimation.repeatCount = ValueAnimator.INFINITE
    }

    private fun buttomAccept(){
        btn_dialog_location.setOnClickListener {
            dismiss()
        }
    }



}