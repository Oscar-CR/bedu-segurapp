package org.bedu.segurapp.ui.getStarted

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.content.res.AppCompatResources.getColorStateList
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.snackbar.Snackbar
import org.bedu.segurapp.R
import org.bedu.segurapp.helpers.clearForm
import org.bedu.segurapp.helpers.makeValidations
import org.bedu.segurapp.helpers.moveNext
import org.bedu.segurapp.models.Contacts
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import org.bedu.segurapp.helpers.hideKeyboard

class GetStartedSafeContactsFragment : Fragment() {

    private lateinit var chipsContainer: ChipGroup
    private lateinit var btnNext: Button
    private lateinit var txtContactName: EditText
    private lateinit var txtNumber: EditText
    private val safeContactsList: MutableList<Contacts> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_get_started_safe_contacts, container, false)
        initComponents(view)
        txtNumberActionListener()
        btnNextClickListener()
        return view
    }

    private fun initComponents(view: View) {
        chipsContainer = view.findViewById(R.id.chips_container)
        btnNext = view.findViewById(R.id.btn_next)
        txtContactName = view.findViewById(R.id.txt_contact_name)
        txtNumber = view.findViewById(R.id.txt_number)
    }

    private fun txtNumberActionListener() {
        txtNumber.setOnEditorActionListener(OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (makeValidations(arrayOf(txtContactName, txtNumber), requireContext())) {
                    addSafeContact(Contacts(R.drawable.unknown,
                        txtContactName.text.toString(),
                        txtNumber.text.toString()))
                    txtNumber.hideKeyboard()
                }
                return@OnEditorActionListener true
            }
            false
        })
    }

    private fun btnNextClickListener() {
        btnNext.setOnClickListener {

            if (safeContactsList.isNotEmpty()) {
                val mPager = (activity as GetStartedActivity).findViewById<ViewPager2>(R.id.pager)
                if (mPager != null) moveNext(mPager)
            } else {
                Snackbar.make(requireView(), resources.getString(R.string.required_safe_contacts_hint), Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun addSafeContact(safeContact: Contacts) {
        safeContactsList.add(safeContact)
        chipsContainer.addView(createChip(safeContact))
        clearForm(arrayOf(txtContactName, txtNumber))
    }

    private fun createChip(safeContact: Contacts): Chip {

        val foregroundColor = ContextCompat.getColor(requireContext(), R.color.segurapp_black)
        val chip = Chip(requireContext())
        chip.text = safeContact.name
        chip.isClickable = true
        chip.isCheckable = false
        chip.isCloseIconVisible = true
        chip.isCheckable = false
        chip.chipBackgroundColor = getColorStateList(requireContext(),
            R.color.segurapp_gray)
        chip.setTextColor(foregroundColor)

        chip.setOnCloseIconClickListener {
            safeContactsList.remove(safeContact)
            chipsContainer.removeView(chip)
        }

        return chip
    }
}