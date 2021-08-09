package org.bedu.segurapp.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import org.bedu.segurapp.ui.getStarted.GetStartedDoneFragment
import org.bedu.segurapp.ui.getStarted.GetStartedProfileConfigurationFragment
import org.bedu.segurapp.ui.getStarted.GetStartedSafeContactsFragment
import org.bedu.segurapp.ui.getStarted.GetStartedSplashFragment

class ViewPagerAdapter(fragment: FragmentActivity) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 4
    override fun createFragment(position: Int): Fragment {
        return when (position + 1) {
            1 -> GetStartedSplashFragment()
            2 -> GetStartedProfileConfigurationFragment()
            3 -> GetStartedSafeContactsFragment()
            4 -> GetStartedDoneFragment()
            else -> GetStartedSplashFragment()
        }
    }
}