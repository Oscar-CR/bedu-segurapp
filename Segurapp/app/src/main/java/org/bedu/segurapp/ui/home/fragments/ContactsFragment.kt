package org.bedu.segurapp.ui.home.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.bedu.segurapp.R
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import org.bedu.segurapp.adapters.ContactAdapter
import org.bedu.segurapp.databinding.FragmentContactBinding
import org.bedu.segurapp.models.SafeContact


class ContactsFragment : Fragment(){


    private lateinit var skeleton: Skeleton
    private lateinit var contactAdapter: ContactAdapter
    private lateinit var binding: FragmentContactBinding
    private var contacts : MutableList<SafeContact> = ArrayList()

    private val mAuth = Firebase.auth
    private val db = Firebase.firestore
    private val usersCollection = db.collection("users")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_contact, container, false)
        binding.btnContactsAdd.setOnClickListener { Navigation.findNavController(it).navigate(R.id.navigateToAddContactFragment) }
        createSkeleton()
        setupContactList()
        return binding.root
    }

    private fun createSkeleton(){
        skeleton = binding.list.applySkeleton(R.layout.item_contact)
        skeleton.showSkeleton()
    }

    private fun initListener() {

        binding.etSearchContact.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                filter(s.toString())
            }
        })

    }

    private fun filter(text:String){
        val filteredListC: MutableList<SafeContact> = ArrayList()
        for (item in contacts) {
            if (item.name.lowercase().contains(text.lowercase())) filteredListC.add(item)
        }
        contactAdapter.filterListC(filteredListC)
    }

    private fun setupContactList(){
        getContacts{contactList ->
            if(contactList.isNotEmpty()){
                skeleton.showOriginal()
                contacts = contactList
                contactAdapter = ContactAdapter(contacts)
                binding.list.adapter = contactAdapter
                initListener()
            }
        }
    }

    private fun getContacts(callback: (MutableList<SafeContact>) -> Unit) {

        mAuth.currentUser?.uid?.let { idUser ->
            usersCollection
                .whereEqualTo("id", idUser)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    if (querySnapshot.documents.isNotEmpty()) {
                        val data = querySnapshot.documents.first().get("channel.members") as ArrayList<HashMap<String, Any>>
                        val mJson = Gson().toJson(data)
                        val contactList = ArrayList(
                            Gson().fromJson(mJson, Array<SafeContact>::class.java).toList()
                        )

                        callback(contactList.toMutableList())
                    }
                }
        }
    }



}


