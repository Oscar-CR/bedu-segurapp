package org.bedu.segurapp.ui.home

import ContactsFragment
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_get_started.*
import kotlinx.android.synthetic.main.activity_home.*
import org.bedu.segurapp.R
import org.bedu.segurapp.ui.home.adapters.MessageAdapter
import org.bedu.segurapp.ui.home.fragments.HomeFragment
import org.bedu.segurapp.ui.home.fragments.MessagesFragment

class HomeActivity : AppCompatActivity() {

    private lateinit var bottom_navigation: BottomNavigationView
    private lateinit var appBar: Toolbar
    private lateinit var nav_view: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        bottom_navigation=findViewById(R.id.bottom_navigation)
        appBar = findViewById(R.id.app_bar)
        nav_view = findViewById(R.id.nav_view)

        bottom_navigation.setSelectedItemId(R.id.page_2);

        this.setSupportActionBar(appBar)

        setupDrawer(appBar)

        val homeFragment= HomeFragment()
        val contactsFragment= ContactsFragment()
        val msgFragment = MessagesFragment()

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

    //Agregar el menú de opciones al AppBar

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_app_bar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    //asignamos las acciones para cada opción del AppBar

    private fun setupDrawer(toolbar: Toolbar){
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        val drawerToggle = ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open_drawer,R.string.close_drawer)
    }

    private fun makeCurretFragment(fragment: Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, fragment)
            commit()
        }


}