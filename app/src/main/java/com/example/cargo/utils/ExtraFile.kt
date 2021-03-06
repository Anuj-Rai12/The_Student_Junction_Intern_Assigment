package com.example.cargo.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Patterns
import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import com.example.cargo.databinding.CustomProgressBarLayoutBinding
import com.google.firebase.auth.FirebaseAuth
import java.util.regex.Pattern
import javax.inject.Inject

const val TAG = "ANUJ"

class MyDialog :
    androidx.fragment.app.DialogFragment() {
    private val args: MyDialogArgs by navArgs()
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val alterDialog = AlertDialog.Builder(requireActivity()).setTitle(args.title)
        alterDialog.setMessage(args.message).setPositiveButton("ok") { dialogInterface, _ ->
            dialogInterface.dismiss()
        }
        return alterDialog.create()
    }
}

class MyLogoutDialog(
    private val title: String = "No Title",
    private val msg: String = "No Message",
    private val function: ((flag: Boolean) -> Unit)
) :
    androidx.fragment.app.DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val alterDialog = AlertDialog.Builder(requireActivity()).setTitle(title)
        alterDialog.setMessage(msg)
        alterDialog.setPositiveButton("LogOut") { _, _ ->
            FirebaseAuth.getInstance().signOut()
            activity?.finish()
        }
            .setNegativeButton("Cancel") { dialogInterface, _ ->
                function(false)
                dialogInterface.dismiss()
            }
        return alterDialog.create()
    }
}

object ExtraFile {
    const val log_out_msg = "Log Out!!"
    const val Base_Url = "https://fakestoreapi.com"
    const val param_key = "/products"
    const val Load_size = 30
    const val show_dialog_once = "My_Dialog_Application_Status"
    const val Rs = "₹"
}


fun msg() = "The Good Password Must contain Following thing ;) :- \n\n" +
        "1.At least 1 digit i.e [0-9]\n" +
        "2.At least 1 lower case letter i.e [a-z]\n" +
        "3.At least 1 upper case letter i.e [A-Z]\n" +
        "4.Any letter i.e [A-Z,a-z]\n" +
        "5.At least 1 special character i.e [%^*!&*|)(%#$%]\n" +
        "6.No white spaces\n" +
        "7.At Least 8 Character\n"


fun isValidEmail(target: CharSequence?): Boolean {
    return if (target == null) {
        false
    } else {
        Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }
}

fun checkFieldValue(string: String) = string.isEmpty() || string.isBlank()

fun isValidPassword(password: String): Boolean {
    val passwordREGEX = Pattern.compile(
        "^" +
                "(?=.*[0-9])" +         //at least 1 digit
                "(?=.*[a-z])" +         //at least 1 lower case letter
                "(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{8,}" +               //at least 8 characters
                "$"
    )
    return passwordREGEX.matcher(password).matches()
}


class CustomProgress @Inject constructor(private val customProgressBar: CustomProgressBar) {
    fun hideLoading() {
        customProgressBar.dismiss()
    }

    @SuppressLint("SourceLockedOrientationActivity")
    fun showLoading(context: Context, string: String?, boolean: Boolean = false) {
        val con = context as Activity
        customProgressBar.show(con, string, boolean)
    }
}

class CustomProgressBar @Inject constructor() {
    private var alertDialog: AlertDialog? = null

    @SuppressLint("SourceLockedOrientationActivity")
    fun show(context: Context, title: CharSequence?, flag: Boolean = true) {
        val con = (context as Activity)
        val alertDialog = AlertDialog.Builder(con)
        val inflater = (con).layoutInflater
        val binding = CustomProgressBarLayoutBinding.inflate(inflater)
        title?.let {
            binding.textView.text = it
        }
        alertDialog.setView(binding.root)
        alertDialog.setCancelable(flag)
        this.alertDialog = alertDialog.create()
        this.alertDialog?.show()
    }

    fun dismiss() = alertDialog?.dismiss()
}

fun fetchDis(currPrice: Double, mrpPrice: Double) =
    (((mrpPrice - currPrice) / mrpPrice) * 100).toInt()

//Utils Function to Hide View
fun View.hide() {
    this.isVisible = false
}

//Utils Function to Show View
fun View.show() {
    this.isVisible = true
}

