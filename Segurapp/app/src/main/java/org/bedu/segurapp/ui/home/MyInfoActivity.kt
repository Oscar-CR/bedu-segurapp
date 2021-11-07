package org.bedu.segurapp.ui.home

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.bedu.segurapp.R
import org.bedu.segurapp.databinding.ActivityMyinfoBinding
import org.bedu.segurapp.helpers.makeFormValidations

class MyInfoActivity : AppCompatActivity() {

    private val binding by lazy {ActivityMyinfoBinding.inflate(layoutInflater)}
    private val db = Firebase.firestore
    private val mAuth = Firebase.auth
    private val usersCollection = db.collection("users")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupActionBar()
        backButton()
        getInfo()
        updateButtonListener()
    }

    private fun setupActionBar() = supportActionBar?.setDisplayHomeAsUpEnabled(true)

    private fun backButton() {
        binding.btnBack.setOnClickListener {
            val i = Intent(this, HomeActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(i)
        }
    }

    private fun getInfo() {
        usersCollection.document(mAuth.currentUser?.uid.toString()).get()
            .addOnSuccessListener {
                if (it.exists()) {
                    binding.edEmail.setText(it.get("name") as String?)
                    binding.edMessage.setText(it.get("message") as String?)
                }
            }
    }

    private fun updateButtonListener(){
        binding.btnUpdate.setOnClickListener {
            if (makeFormValidations(arrayOf(binding.edMessage), applicationContext)) {
                usersCollection.document(mAuth.currentUser?.uid.toString()).update("message", binding.edMessage.text.toString()).addOnCompleteListener { response ->
                    if (response.isSuccessful) {
                        Toast.makeText(applicationContext, getString(R.string.success_update_profile), Toast.LENGTH_SHORT).show()
                        val bundle = Bundle()
                        val returnHome = Intent(this, HomeActivity::class.java).apply { putExtras(bundle) }
                        startActivity(returnHome)
                    }
                }
            }
        }
    }

}


