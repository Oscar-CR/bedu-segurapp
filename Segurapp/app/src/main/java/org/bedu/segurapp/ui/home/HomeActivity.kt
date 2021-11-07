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
import android.view.MenuItem
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.*
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.mapbox.mapboxsdk.Mapbox
import org.bedu.segurapp.R
import org.bedu.segurapp.databinding.ActivityHomeBinding
import org.bedu.segurapp.models.AirplaneReceiver
import android.view.View
import org.bedu.segurapp.models.Preferences


class HomeActivity : AppCompatActivity() {

    private val binding by lazy {ActivityHomeBinding.inflate(layoutInflater)}
    private val airplaneReceiver = AirplaneReceiver()
    private lateinit var analytics: FirebaseAnalytics
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    companion object {
        const val CHANNEL_HELP = "CHANNEL_HELP"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token))
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.homeFragment,R.id.socialSecurityFragment,R.id.contactsFragment, R.id.myInfoActivity, R.id.aboutActivity,R.id.privacyActivity, R.id.logoutActivity),
            binding.drawerLayout
        )

        setHeader()
        sdkValidator()
        airplaneMode()
        navigationOptions()

    }

    private fun setHeader(){
        val header: View = binding.navView.getHeaderView(0)
        val txtMessage: TextView = header.findViewById(R.id.tvWelcomeMessage)
        val txtEmail: TextView = header.findViewById(R.id.tvUserEmail)
        txtMessage.text = "Bienvenido"
        txtEmail.text = Preferences(applicationContext).getName()
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

    }

    //Navegación con graph
    private fun navigationOptions(){
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)as NavHostFragment
        navController = navHostFragment.findNavController()

        //Toolbar
        setSupportActionBar(binding.appBar)
        setupActionBarWithNavController(navController, appBarConfiguration)

        //Bottom Navigation
        binding.bottomNavigation.setupWithNavController(navController)

        // DrawerLayout
        binding.navView.setupWithNavController(navController)
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

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }

}