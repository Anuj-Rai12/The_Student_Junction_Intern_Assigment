package com.example.cargo.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cargo.R
import com.example.cargo.databinding.RegistrationFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment(R.layout.registration_fragment) {
    private lateinit var binding: RegistrationFragmentBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = RegistrationFragmentBinding.bind(view)
        binding.registerBtn.setOnClickListener {
            val action = SignUpFragmentDirections.actionSignUpFragmentToShoppingDashBoardFragment()
            findNavController().navigate(action)
        }
        binding.signBtn.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}