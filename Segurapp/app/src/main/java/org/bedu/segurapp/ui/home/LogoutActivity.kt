package org.bedu.segurapp.ui.home

import android.animation.ValueAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import com.airbnb.lottie.LottieAnimationView
import org.bedu.segurapp.R
import org.bedu.segurapp.UserLogin.Companion.pref

class LogoutActivity : AppCompatActivity() {

    private lateinit var exitAnimation: LottieAnimationView
    private lateinit var btnExit: Button
    private lateinit var btnHome: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logout)


        initComponents()
        setAnimation()
        buttonClick()

    }
    private fun initComponents() {
        exitAnimation = findViewById(R.id.exit_animation)
        btnExit = findViewById(R.id.btn_exit)
        btnHome = findViewById(R.id.btn_back)

    }

    private fun setAnimation() {
        exitAnimation.setAnimation("robot.json")
        exitAnimation.playAnimation()
        exitAnimation.repeatCount = ValueAnimator.INFINITE
    }

    private fun buttonClick(){
        btnExit.setOnClickListener {
            alertExit()

        }

        btnHome.setOnClickListener {
            finish()
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
        finishAffinity()
    }

}