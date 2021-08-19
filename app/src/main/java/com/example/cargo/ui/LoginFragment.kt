package com.example.cargo.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.cargo.R
import com.example.cargo.databinding.LoginFragmentScreenBinding
import com.example.cargo.utils.*
import com.example.cargo.viewmodel.MyViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.login_fragment_screen) {
    private lateinit var binding: LoginFragmentScreenBinding
    private var email: String? = null
    private var password: String? = null
    private val viewModel: MyViewModel by viewModels()

    @Inject
    lateinit var customProgress: CustomProgress
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        savedInstanceState?.let {
            email = it.getString(ExtraFile.log_out_msg)
            password = it.getString(ExtraFile.show_dialog_once)
        }
        email?.let {
            userAccount(it, password!!)
        }
        binding = LoginFragmentScreenBinding.bind(view)
        binding.loginBtn.setOnClickListener {
            val email = binding.emailText.text.toString()
            val pass = binding.passwordText.text.toString()
            if (!isValidEmail(email)) {
                Snackbar.make(requireView(), getString(R.string.wrong_email), Snackbar.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            if (!isValidPassword(pass)) {
                Snackbar.make(
                    requireView(),
                    getString(R.string.wrong_password),
                    Snackbar.LENGTH_LONG
                ).setAction("INFO") {
                    dir(msg = msg(), title = "Good Password :)")
                }.show()
                return@setOnClickListener
            }
            userAccount(email = email, password = pass)
        }
        binding.signInBtn.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToSignUpFragment()
            findNavController().navigate(action)
        }
    }

    private fun showLoading(string: String) = customProgress.showLoading(requireActivity(), string)
    private fun hideLoading() = customProgress.hideLoading()

    private fun dir(choose: Int = 0, title: String = "Error", msg: String = "") {
        val action = when (choose) {
            0 -> LoginFragmentDirections.actionGlobalMyDialog(title = title, message = msg)
            else -> LoginFragmentDirections.actionLoginFragmentToShoppingDashBoardFragment()
        }
        findNavController().navigate(action)
    }

    override fun onStart() {
        super.onStart()
        if (FirebaseAuth.getInstance().currentUser != null) {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToShoppingDashBoardFragment())
        }
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        email?.let { email ->
            outState.putString(ExtraFile.log_out_msg, email)
        }
        password?.let { pass ->
            outState.putString(ExtraFile.show_dialog_once, pass)
        }
    }

    override fun onPause() {
        super.onPause()
        hideLoading()
    }

    private fun userAccount(email: String, password: String) {
        this.email = email
        this.password = password
        viewModel.userLogin(email, password).observe(viewLifecycleOwner) {
            when (it) {
                is MySealed.Error -> {
                    hideLoading()
                    this.email = null
                    this.password = null
                    dir(msg = it.exception?.localizedMessage ?: "UnWanted Error")
                }
                is MySealed.Loading -> showLoading(it.data as String)
                is MySealed.Success -> {
                    hideLoading()
                    this.email = null
                    this.password = null
                    dir(23)
                }
            }
        }
    }
}