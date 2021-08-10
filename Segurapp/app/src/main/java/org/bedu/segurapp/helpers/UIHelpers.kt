package org.bedu.segurapp.helpers

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.viewpager2.widget.ViewPager2

fun moveNext(mPager: ViewPager2) {
    mPager.currentItem = mPager.currentItem + 1
}

fun movePrevious(mPager: ViewPager2) {
    mPager.currentItem = mPager.currentItem - 1
}

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}