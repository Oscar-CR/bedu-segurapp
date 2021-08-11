package org.bedu.segurapp.ui.getStarted

import android.animation.ValueAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.viewpager2.widget.ViewPager2
import com.airbnb.lottie.LottieAnimationView
import org.bedu.segurapp.R
import org.bedu.segurapp.helpers.moveNext

class GetStartedSplashFragment : Fragment() {

    private lateinit var mPager: ViewPager2
    private lateinit var mainAnimation: LottieAnimationView
    private lateinit var btnContinueTitle: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_get_started_splash, container, false)
        initComponents(view)
        setAnimation()
        btnContinueTitleClickListener()
        return view
    }

    private fun initComponents(view: View) {
        mPager = (activity as GetStartedActivity).findViewById(R.id.pager)
        mainAnimation = view.findViewById(R.id.main_animation)
        btnContinueTitle = view.findViewById(R.id.btn_continue_title)
    }

    private fun setAnimation() {
        mainAnimation.setAnimation("hello.json")
        mainAnimation.playAnimation()
        mainAnimation.repeatCount = ValueAnimator.INFINITE
    }

    private fun btnContinueTitleClickListener() {
        btnContinueTitle.setOnClickListener {
            moveNext(mPager)
        }
    }
}