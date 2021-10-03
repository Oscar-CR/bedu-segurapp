package org.bedu.segurapp.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.bedu.segurapp.R
import org.bedu.segurapp.databinding.ActivityAddContactBinding
import org.bedu.segurapp.ui.home.fragments.AddContactFragment

class AddContactActivity : AppCompatActivity() {

    private val binding by lazy { ActivityAddContactBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        val fragment = AddContactFragment()
        val fragmentManager = supportFragmentManager

        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.add_container, fragment)
        fragmentTransaction.commit()

    }


}