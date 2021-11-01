package org.bedu.segurapp.ui.home.fragments

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import org.bedu.segurapp.R
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import org.bedu.segurapp.adapters.ContactAdapter
import org.bedu.segurapp.databinding.FragmentContactBinding
import org.bedu.segurapp.models.ContactViewModel
import org.bedu.segurapp.UserLogin
import org.bedu.segurapp.models.local.Contact
import org.bedu.segurapp.ui.home.AddContactActivity


class ContactsFragment : Fragment(){


    private lateinit var contactAdapter: ContactAdapter
    private lateinit var viewModel: ContactViewModel
    private lateinit var binding: FragmentContactBinding
    private lateinit var etSearchContact: EditText
    private lateinit var contacts: MutableList<Contact>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_contact, container, false)

        viewModel = ContactViewModel(
            (requireContext().applicationContext as UserLogin).contactRepository
        )

        binding.lifecycleOwner = this
        binding.contactViewModel = viewModel



        /*binding.btnContactsAdd.setOnClickListener {

            val intent=Intent(context, AddContactActivity::class.java)
            startActivity(intent)

        }*/

        binding.btnContactsAdd.setOnClickListener { Navigation.findNavController(it).navigate(R.id.navigateToAddContactFragment) }

        setupContactList()

        return binding.root
    }

    private fun initComponents(view: View){
        etSearchContact = view.findViewById(R.id.etSearchContact)
    }


    private fun initListener() {

        etSearchContact.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {


            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }
            override fun afterTextChanged(s: Editable) {
                filter(s.toString())
            }
        })

    }

    private fun filter(text:String){

        val filteredListC: MutableList<Contact> = ArrayList()
        for (item in contacts) {
            if (item.name?.lowercase()?.contains(text.lowercase()) == true) {
                filteredListC.add(item)

            }
        }
        contactAdapter.filterListC(filteredListC);
    }


    //Infla el recyclerview
    private fun setupContactList(){

        contactAdapter = ContactAdapter(viewModel)
        binding.list.adapter = contactAdapter

        viewModel.contactList.observe(viewLifecycleOwner, Observer {
            it?.let {
               contactAdapter.submitList(it)
            }
        })

    }



}


