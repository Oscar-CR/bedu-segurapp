package org.bedu.segurapp.helpers

import androidx.viewpager2.widget.ViewPager2

fun moveNext(mPager: ViewPager2) {
    mPager.currentItem = mPager.currentItem + 1
}

fun movePrevious(mPager: ViewPager2) {
    mPager.currentItem = mPager.currentItem - 1
}