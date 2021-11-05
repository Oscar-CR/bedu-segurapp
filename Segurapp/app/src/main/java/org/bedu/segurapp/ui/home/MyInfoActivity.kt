package org.bedu.segurapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_myinfo.*
import kotlinx.android.synthetic.main.activity_register.*
import org.bedu.segurapp.R

class MyInfoActivity : AppCompatActivity() {

    private val db = Firebase.firestore
    private val mAuth = Firebase.auth
    //private val db = FirebaseFirestore.getInstance()
    private val users = db.collection("users")

    lateinit var miName: EditText
    lateinit var miLastName: EditText
    lateinit var miMessage: EditText
    lateinit var btnBack: ImageView
    lateinit var btnGetInfo: Button
    lateinit var btnUpdate: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_myinfo)
        viewInitializations()

        backButtom()
        getInfo()
    }




    private fun viewInitializations() {

        miName = findViewById(R.id.mi_first_name)
        //miLastName = findViewById(R.id.mi_last_name)
        miMessage = findViewById(R.id.mi_message)
        btnBack = findViewById(R.id.btn_back)
        btnGetInfo = findViewById(R.id.btn_Get_Info)
        btnUpdate = findViewById(R.id.btn_Update)


        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    private fun validateInput(): Boolean {
        if (miName.text.toString() == "") {
            miName.error = "Por favor escribe tu nombre"
            return false
        }

        /*if (miLastName.text.toString() == "") {
            miLastName.error = "Por favor escribe tu apellido"
            return false
        }*/

        if (miMessage.text.toString() == "") {
            miMessage.error = "Por favor escribe tu mensaje de ayuda"
            return false
        }

        return true
    }


    fun performEditProfile(view: View) {
        btnUpdate.setOnClickListener {

            if (validateInput()) {
                miName.text.toString()
                //miLastName.text.toString()
                miMessage.text.toString()

                val users = db.collection("users")
                val updateData = hashMapOf(
                    "name" to mi_first_name.text.toString(),
                    //"last" to "",
                    "message" to mi_message.text.toString()
                )
                users.document(mAuth.currentUser?.uid.toString()).update("message", mi_message.text.toString()).addOnCompleteListener{
                        response ->
                    if (response.isSuccessful) {
                        Toast.makeText(this, "Has actualizado tu perfil", Toast.LENGTH_SHORT).show()
                        val bundle = Bundle()
                        val returnhome = Intent(this, HomeActivity::class.java).apply { putExtras(bundle) }
                        startActivity(returnhome)
                    }
                }



            }
        }
    }

    private fun backButtom() {
        btnBack.setOnClickListener {
            val i = Intent(this, HomeActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(i)
        }
    }


    private fun getInfo() {
        btnGetInfo.setOnClickListener {
            db.collection("users").document(mAuth.currentUser?.uid.toString()).get().addOnSuccessListener {
                mi_first_name.setText(it.get("name") as String?)
                mi_message.setText(it.get("message") as String?)
            }
        }
    }



    /*

    private fun updateInfo() {
        btnUpdate.setOnClickListener{
            val users = db.collection("users")

            val updateData = hashMapOf(
                "name" to mi_first_name.text.toString(),
                //"last" to "",
                "message" to mi_message.text.toString()
            )
            users.document("").set(updateData)
        }

    }
*/


    //private fun showInfo(name: String, message: String){
    //users.whereEqualTo("users.name", users).get()

    //}
}


