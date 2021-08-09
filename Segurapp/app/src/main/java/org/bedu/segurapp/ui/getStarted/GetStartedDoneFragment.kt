package org.bedu.segurapp.ui.getStarted

import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.airbnb.lottie.LottieAnimationView
import org.bedu.segurapp.R
import org.bedu.segurapp.ui.home.HomeActivity


class GetStartedDoneFragment : Fragment() {
    private lateinit var mainAnimation: LottieAnimationView
    private lateinit var btnAccept: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_get_started_done, container, false)
        initComponents(view)
        setAnimation()
        btnAcceptClickListener()
        return view
    }

    private fun initComponents(view: View) {
        mainAnimation = view.findViewById(R.id.main_animation)
        btnAccept = view.findViewById(R.id.btn_accept)
    }

    private fun setAnimation() {
        mainAnimation.setAnimation("done.json")
        mainAnimation.playAnimation()
        mainAnimation.repeatCount = ValueAnimator.INFINITE
    }

    private fun btnAcceptClickListener() {
        btnAccept.setOnClickListener {
            val intent = Intent(requireActivity(), HomeActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }
}