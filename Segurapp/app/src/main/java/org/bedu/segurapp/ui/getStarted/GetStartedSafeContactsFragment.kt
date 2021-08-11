package org.bedu.segurapp.ui.getStarted

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
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
import org.bedu.segurapp.models.Contacts
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import androidx.activity.result.contract.ActivityResultContracts
import com.araujo.jordan.excuseme.ExcuseMe
import com.google.gson.Gson
import org.bedu.segurapp.helpers.*
import org.bedu.segurapp.utils.getNameByUri
import org.bedu.segurapp.utils.getPhoneByUri

class GetStartedSafeContactsFragment : Fragment() {

    private lateinit var mPager: ViewPager2
    private lateinit var chipsContainer: ChipGroup
    private lateinit var btnSafeContacts: Button
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
        btnSafeContactsClickListener()
        txtNumberActionListener()
        btnNextClickListener()
        return view
    }

    override fun onPause() {
        if (safeContactsList.isEmpty() && mPager.currentItem == 3) {
            movePrevious(mPager)
            setSafeContactMessage()
        }

        super.onPause()
    }

    private val resultLauncher = this.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intentResult: Intent? = result.data
                intentResult?.data?.let {
                    renderContact(it)
                }
            }
        }

    private fun initComponents(view: View) {
        mPager = (activity as GetStartedActivity).findViewById(R.id.pager)
        chipsContainer = view.findViewById(R.id.chips_container)
        btnSafeContacts = view.findViewById(R.id.btn_safe_contacts)
        btnNext = view.findViewById(R.id.btn_next)
        txtContactName = view.findViewById(R.id.txt_contact_name)
        txtNumber = view.findViewById(R.id.txt_number)
    }

    private fun btnSafeContactsClickListener() {
        btnSafeContacts.setOnClickListener {
            ExcuseMe.couldYouGive(this).permissionFor(
                Manifest.permission.READ_CONTACTS,
            ) {
                if (it.granted.contains(Manifest.permission.READ_CONTACTS)) {
                    openContactsList()
                } else {

                    snackBarMaker(requireContext(),
                        requireView(),
                        R.string.denied_read_contacts_permission_message_hint,
                        Snackbar.LENGTH_INDEFINITE)
                        .setAction(getString(R.string.permission_configuration_hint)) {
                            openAppPermissionsScreen(requireActivity())
                        }
                        .show()

                }
            }
        }
    }

    private fun txtNumberActionListener() {
        txtNumber.setOnEditorActionListener(OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (makeFormValidations(arrayOf(txtContactName, txtNumber), requireContext())) {
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
            if (safeContactsList.isNotEmpty()) moveNext(mPager)
            else setSafeContactMessage()
        }
    }

    private fun addSafeContact(safeContact: Contacts) {
        safeContactsList.add(safeContact)
        updateContactList()
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
        chip.chipBackgroundColor = getColorStateList(requireContext(), R.color.segurapp_gray)
        chip.setTextColor(foregroundColor)
        chip.setOnCloseIconClickListener {
            safeContactsList.remove(safeContact)
            chipsContainer.removeView(chip)
        }
        return chip
    }

    private fun setSafeContactMessage() {
        snackBarMaker(requireContext(),
            requireView(),
            R.string.required_safe_contacts_hint,
            Snackbar.LENGTH_LONG)
            .show()
    }

    private fun openContactsList() {
        val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
        return resultLauncher.launch(intent)
    }

    private fun renderContact(uri: Uri) {
        val name = getNameByUri(uri, requireActivity().contentResolver)
        val phone = getPhoneByUri(uri, requireActivity().contentResolver)
        if (phone != null && name != null) addSafeContact(Contacts(R.drawable.unknown,
            name,
            formatTelephone(phone)))
        else snackBarMaker(requireContext(),
            requireView(),
            R.string.add_safe_contacts_manually_hint,
            Snackbar.LENGTH_INDEFINITE,
            listOf(name))
            .setAction(resources.getString(R.string.permission_configuration_hint)) {}
            .show()
    }

    private fun updateContactList() {
        val preferences = setSharedPreferences(requireActivity())
        val preferencesEditor = preferences.edit()
        preferencesEditor.putString(getString(R.string.shared_preferences_safe_contact_list),
            Gson().toJson(safeContactsList))
        preferencesEditor.apply()
    }
}


