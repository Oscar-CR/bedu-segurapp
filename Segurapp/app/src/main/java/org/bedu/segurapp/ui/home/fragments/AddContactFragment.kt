package org.bedu.segurapp.ui.home.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.airbnb.lottie.LottieAnimationView
import org.bedu.segurapp.R
import org.bedu.segurapp.databinding.FragmentAddContactBinding
import org.bedu.segurapp.models.AddContactViewModel
import org.bedu.segurapp.UserLogin
import org.bedu.segurapp.ui.home.HomeActivity

class AddContactFragment : Fragment() {

    private lateinit var loginAnimation: LottieAnimationView

    private lateinit var viewModel: AddContactViewModel
    private lateinit var binding: FragmentAddContactBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
             binding = DataBindingUtil.inflate(inflater,R.layout.fragment_add_contact,container,
            false)

        viewModel = AddContactViewModel(
            (requireContext().applicationContext as UserLogin).contactRepository
        )

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.btnSaveContact.setOnClickListener{
            Toast.makeText(context ,"Contacto agregado correctamente", Toast.LENGTH_LONG).show()
            viewModel.newContact()




        }

        setAnimation()
        return binding.root
    }

    private fun setAnimation() {
        with(binding){
            loginAnimation.setAnimation("user.json")
            loginAnimation.playAnimation()
        }

    }


}