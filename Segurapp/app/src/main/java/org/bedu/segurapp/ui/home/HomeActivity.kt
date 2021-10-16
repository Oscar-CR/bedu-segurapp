package org.bedu.segurapp.ui.home

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.mapbox.mapboxsdk.Mapbox
import kotlinx.android.synthetic.main.activity_home.*
import org.bedu.segurapp.R
import org.bedu.segurapp.databinding.ActivityHomeBinding
import org.bedu.segurapp.models.AirplaneReceiver
import org.bedu.segurapp.UserLogin.Companion.pref
import androidx.navigation.ui.setupWithNavController

class HomeActivity : AppCompatActivity() {

    private val binding by lazy {ActivityHomeBinding.inflate(layoutInflater)}
    private val airplaneReceiver = AirplaneReceiver()
    private lateinit var analytics: FirebaseAnalytics

    companion object {
        const val CHANNEL_HELP = "CHANNEL_HELP"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token))
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        sdkValidator()
        airplaneMode()
        buttomNavigationMenu()
        drawerNav()

    }

    private fun sdkValidator(){
        analytics = Firebase.analytics
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setNotificationHelp()
        }
    }

    // Modo avión
    private fun airplaneMode(){
        IntentFilter().apply {
            addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        }.also { filter -> registerReceiver(airplaneReceiver,filter) }

        with(binding){
            setSupportActionBar(appBar)
            setupDrawer(appBar)
        }
    }

    //Navegación simplificada del menu inferior con Navigation Component
    private fun buttomNavigationMenu(){
        val buttonNavigationView = binding.bottomNavigation
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)as NavHostFragment
        val navController = navHostFragment.navController
        buttonNavigationView.setupWithNavController(navController)
    }

    //Agregar el menú de opciones al AppBar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_app_bar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    //asignamos las acciones para cada opción del AppBar
    private fun setupDrawer(toolbar: Toolbar) {
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        val drawerToggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.open_drawer,
            R.string.close_drawer
        )
    }


    //Barra lateral de navegacion
    private fun drawerNav(){
        nav_view.setNavigationItemSelectedListener { menuItem ->
            // Handle menu item selected
            when(menuItem.itemId){
                R.id.nav_messages -> {
                    bottom_navigation.selectedItemId = R.id.socialSecurityFragment
                    drawer_layout.closeDrawer(GravityCompat.START)
                    // loadFragment(MessagesFragment())
                }
                R.id.nav_home -> {
                    bottom_navigation.selectedItemId = R.id.socialSecurityFragment
                    drawer_layout.closeDrawer(GravityCompat.START)
                    // loadFragment(HomeFragment())
                }
                R.id.nav_contacts -> {
                    bottom_navigation.selectedItemId = R.id.socialSecurityFragment
                    drawer_layout.closeDrawer(GravityCompat.START)
                    // loadFragment(org.bedu.segurapp.ui.home.fragments.ContactsFragment())
                }
                R.id.nav_myInfo -> {
                    val bundle = Bundle()
                    val intent = Intent(this, MyInfoActivity::class.java).apply { putExtras(bundle) }
                    startActivity(intent)
                }
                R.id.nav_about -> {
                    intent = Intent(applicationContext, AboutActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_privacy ->{
                    Toast.makeText(this,R.string.drawer_privacy, Toast.LENGTH_SHORT).show()
                }
                R.id.nav_logout -> {
                    drawer_layout.closeDrawer(GravityCompat.START)
                    alertExit()
                }

            }
            true
        }
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

    //Canal para enviar notificaciones
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setNotificationHelp() {
        val name = getString(R.string.channel_help)
        val descriptionText = getString(R.string.courses_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_HELP, name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(airplaneReceiver)
    }

}