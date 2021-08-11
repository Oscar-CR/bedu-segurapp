package org.bedu.segurapp.helpers

import android.app.AlertDialog
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.snackbar.Snackbar

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

fun snackBarMaker(
    context: Context,
    view: View,
    messageId: Int,
    snackBarLength: Int,
    extras: List<String?> = ArrayList(),
): Snackbar {

    return if (extras.isEmpty()) {
        Snackbar.make(view, context.getString(messageId),
            snackBarLength)
    } else {
        Snackbar.make(view, context.getString(messageId, extras.joinToString()),
            snackBarLength)
    }

}


fun alertDialogMaker(
    context: Context,
    titleId: Int,
    messageId: Int,
    cancelable: Boolean = true,
): AlertDialog.Builder {
    val builder = AlertDialog.Builder(context)
    builder.setTitle(context.getString(titleId))
    builder.setMessage(context.getString(messageId))
    builder.setCancelable(cancelable)
    return builder
}