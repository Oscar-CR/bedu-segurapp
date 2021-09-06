package org.bedu.segurapp.ui.getStarted

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.viewpager2.widget.ViewPager2
import com.airbnb.lottie.LottieAnimationView
import com.google.gson.Gson
import org.bedu.segurapp.R
import org.bedu.segurapp.helpers.alertDialogMaker
import org.bedu.segurapp.helpers.movePrevious
import org.bedu.segurapp.helpers.setSharedPreferences
import org.bedu.segurapp.models.Contacts
import org.bedu.segurapp.models.User
import org.bedu.segurapp.models.UserLogin
import org.bedu.segurapp.ui.home.HomeActivity

class GetStartedDoneFragment : Fragment() {

    private lateinit var preferences: SharedPreferences
    private lateinit var mainAnimation: LottieAnimationView
    private lateinit var btnAccept: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_get_started_done, container, false)
        initComponents(view)
        setAnimation()
        btnAcceptClickListener()
        return view
    }


    override fun onPause() {
        btnAccept.isEnabled = false
        super.onPause()
    }

    override fun onResume() {
        processValidation()
        super.onResume()
    }

    @SuppressLint("CommitPrefEdits")
    private fun isAValidUser(): Boolean {
        preferences = setSharedPreferences(requireActivity())
        val userString =
            preferences.getString(resources.getString(R.string.shared_preferences_current_user),
                null)
        val userObject = if (userString != null && userString != "") Gson().fromJson(userString,
            User::class.java) else null

        userObject?.email?.let { UserLogin.pref.saveName(it) }

        return userObject != null && userObject.alertMessage != ""
    }

    @SuppressLint("CommitPrefEdits")
    private fun validateSafeContactsList(): Boolean {
        val preferences = setSharedPreferences(requireActivity())
        val safeContactsListString =
            preferences.getString(resources.getString(R.string.shared_preferences_safe_contact_list),
                null)
        val safeContactsList =
            if (safeContactsListString != null && safeContactsListString != "") Gson().fromJson(
                safeContactsListString,
                Array<Contacts>::class.java) else null
        return safeContactsList != null && safeContactsList.isNotEmpty()
    }

    private fun initComponents(view: View) {
        mainAnimation = view.findViewById(R.id.main_animation)
        btnAccept = view.findViewById(R.id.btn_accept)
    }

    private fun processValidation() {
        if (!isAValidUser()) {
            btnAccept.isEnabled = false
            createDialog()
        } else btnAccept.isEnabled = true
    }

    private fun setAnimation() {
        mainAnimation.setAnimation("done.json")
        mainAnimation.playAnimation()
        mainAnimation.repeatCount = ValueAnimator.INFINITE
    }

    private fun btnAcceptClickListener() {
        btnAccept.setOnClickListener {
            if (validateSafeContactsList()) {
                navigateToHome()
            }
        }
    }

    private fun createDialog() {
        alertDialogMaker(requireContext(),
            R.string.incomplete_fields_title,
            R.string.incomplete_fields,
            true)
            .setPositiveButton(getString(R.string.permission_configuration_hint)) { _: DialogInterface, _: Int ->
                val mPager = (activity as GetStartedActivity).findViewById<ViewPager2>(R.id.pager)
                if (mPager != null) movePrevious(mPager)
            }
            .show()
    }

    private fun navigateToHome() {
        val intent = Intent(requireActivity(), HomeActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }
}