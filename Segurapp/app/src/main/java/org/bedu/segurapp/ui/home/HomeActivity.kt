package org.bedu.segurapp.ui.home

import ContactsFragment
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_home.*
import org.bedu.segurapp.R
import org.bedu.segurapp.databinding.ActivityHomeBinding
import org.bedu.segurapp.models.UserLogin.Companion.pref
import org.bedu.segurapp.ui.home.fragments.HomeFragment
import org.bedu.segurapp.ui.home.fragments.MessagesFragment

class HomeActivity : AppCompatActivity() {

    private val binding by lazy {ActivityHomeBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        bottom_navigation.selectedItemId = R.id.page_2;

        with(binding){
            setSupportActionBar(appBar)
            setupDrawer(appBar)
        }

        drawerNav()

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

    //Barra lateral de navegacion
    private fun drawerNav(){
        nav_view.setNavigationItemSelectedListener { menuItem ->
            // Handle menu item selected
            when(menuItem.itemId){
                R.id.nav_messages -> {
                    bottom_navigation.selectedItemId = R.id.page_1;
                    drawer_layout.closeDrawer(GravityCompat.START)
                    // loadFragment(MessagesFragment())
                }
                R.id.nav_home -> {
                    bottom_navigation.selectedItemId = R.id.page_2;
                    drawer_layout.closeDrawer(GravityCompat.START)
                    // loadFragment(HomeFragment())
                }
                R.id.nav_contacts -> {
                    bottom_navigation.selectedItemId = R.id.page_3;
                    drawer_layout.closeDrawer(GravityCompat.START)
                    // loadFragment(ContactsFragment())
                }
                R.id.nav_myInfo -> {
                    Toast.makeText(this,"Informacion", Toast.LENGTH_SHORT).show()
                }
                R.id.nav_about -> {
                    Toast.makeText(this,"Acerca de", Toast.LENGTH_SHORT).show()
                }
                R.id.nav_privacy ->{
                    Toast.makeText(this,"Privacidad", Toast.LENGTH_SHORT).show()
                }
                R.id.nav_logout -> {
                    drawer_layout.closeDrawer(GravityCompat.START)
                    alertExit()
                }

            }

            true
        }

    }

    // Infla el fragmento en la Actvidad Home (frame_container)
    fun loadFragment(fragment: Fragment?) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment!!)
        transaction.commit()
        drawer_layout.closeDrawer(GravityCompat.START)
    }

    private fun alertExit(){

        // Crea el alert dialog
        val dialogBuilder = AlertDialog.Builder(this)

        // Setea el mensaje
        dialogBuilder.setMessage("¿Desea salir de su cuenta?")
            // si el boton es cancelable, es decir, no se puede salir de este dialogo
            .setCancelable(false)
            // opcion Aceptar
            .setPositiveButton("Aceptar") { _ , _ ->
                logout()   //finish()
            }
            // Opcion cancelar
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.cancel()
            }

        // Crea la caja de dialogo
        val alert = dialogBuilder.create()
        // Setea el titulo del alert dialog
        alert.setTitle("Cerrar sesión")
        // Muestra el alert dialog
        alert.show()
    }


    //Cerrar sesion en shared preferences
    private fun logout() {
        pref.wipe()
        onBackPressed()
    }
}