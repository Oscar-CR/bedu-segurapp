package org.bedu.segurapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.bedu.segurapp.R
import org.bedu.segurapp.fragments.ContactsFragment
import org.bedu.segurapp.fragments.HomeFragment
import org.bedu.segurapp.fragments.SmsFragment

class MenuActivity : AppCompatActivity() {

    private  lateinit var bottom_navigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        bottom_navigation=findViewById(R.id.bottom_navigation)

        val homeFragment=HomeFragment()
        val contactsFragment= ContactsFragment()
        val msgFragment = SmsFragment()

        makeCurretFragment(homeFragment)

        bottom_navigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.page_1 -> makeCurretFragment(msgFragment)
                R.id.page_2 -> makeCurretFragment(homeFragment)
                R.id.page_3 -> makeCurretFragment(contactsFragment)
            }
            true
        }

    }

    private fun makeCurretFragment(fragment: Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, fragment)
            commit()
        }
}