package com.example.cargo.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.cargo.R
import com.example.cargo.databinding.RegistrationFragmentBinding
import com.example.cargo.utils.*
import com.example.cargo.viewmodel.MyViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignUpFragment : Fragment(R.layout.registration_fragment) {
    private lateinit var binding: RegistrationFragmentBinding
    private var email: String? = null
    private var password: String? = null
    private val viewModel: MyViewModel by viewModels()

    @Inject
    lateinit var customProgress: CustomProgress
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = RegistrationFragmentBinding.bind(view)
        savedInstanceState?.let {
            email = it.getString(ExtraFile.log_out_msg)
            password = it.getString(ExtraFile.show_dialog_once)
        }
        email?.let {
            createUserAccount(it, password!!)
        }
        binding.registerBtn.setOnClickListener {
            val firstName = binding.firstName.text.toString()
            val lastName = binding.lastName.text.toString()
            val email = binding.emailAddress.text.toString()
            val pass = binding.password.text.toString()
            if (checkFieldValue(firstName)
                || checkFieldValue(lastName) ||
                checkFieldValue(email) ||
                checkFieldValue(pass)
            ) {
                Snackbar.make(
                    requireView(),
                    getString(R.string.wrong_detail),
                    Snackbar.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
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
            createUserAccount(email = email, password = pass)
        }
        binding.signBtn.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun showLoading(string: String) = customProgress.showLoading(requireActivity(), string)
    private fun hideLoading() = customProgress.hideLoading()

    private fun createUserAccount(email: String, password: String) {
        this.email = email
        this.password = password
        viewModel.createUserAccount(email, password).observe(viewLifecycleOwner) {
            when (it) {
                is MySealed.Error -> {
                    hideLoading()
                    this.email = null
                    this.password = null
                    dir(msg = it.exception?.localizedMessage ?: "UnWanted Error")
                }
                is MySealed.Loading -> showLoading(it.data as String)
                is MySealed.Success -> {
                    this.email = null
                    this.password = null
                    hideLoading()
                    dir(23)
                }
            }
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
    private fun dir(choose: Int = 0, title: String = "Error", msg: String = "") {
        val action = when (choose) {
            0 -> SignUpFragmentDirections.actionGlobalMyDialog(title = title, message = msg)
            else -> SignUpFragmentDirections.actionSignUpFragmentToShoppingDashBoardFragment()
        }
        findNavController().navigate(action)
    }
}