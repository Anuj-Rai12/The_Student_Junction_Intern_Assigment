package com.example.cargo.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cargo.R
import com.example.cargo.databinding.LoginFragmentScreenBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.login_fragment_screen) {
    private lateinit var binding: LoginFragmentScreenBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = LoginFragmentScreenBinding.bind(view)
        binding.loginBtn.setOnClickListener {
            val action=LoginFragmentDirections.actionLoginFragmentToShoppingDashBoardFragment()
            findNavController().navigate(action)
        }
        binding.signInBtn.setOnClickListener {
            val action=LoginFragmentDirections.actionLoginFragmentToSignUpFragment()
            findNavController().navigate(action)
        }
    }
}