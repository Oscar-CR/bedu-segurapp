package org.bedu.segurapp.helpers

import androidx.viewpager2.widget.ViewPager2
import org.bedu.segurapp.R
import org.bedu.segurapp.ui.getStarted.GetStartedActivity


fun moveNext(activity: GetStartedActivity) {
    val mPager = activity.findViewById<ViewPager2>(R.id.pager)
    mPager.currentItem = mPager.currentItem + 1
}
