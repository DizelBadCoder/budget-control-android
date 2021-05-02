package dizel.budget_control.auth.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import dizel.budget_control.budget_list.view.MainActivity
import dizel.budget_control.R
import dizel.budget_control.auth.isValidEmail
import dizel.budget_control.auth.isValidPassword
import dizel.budget_control.databinding.FragmentSingInBinding

class SignInFragment: Fragment(R.layout.fragment_sing_in) {
    private var _binding: FragmentSingInBinding? = null
    private val binding get() = _binding!!


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentSingInBinding.bind(view).apply {
            vSignInButton.setOnClickListener { signInUser() }
        }
    }

    private fun signInUser() {
        val email = binding.vEditTextEmail.text.toString()
        val password = binding.vEditTextPassword.text.toString()

        if (!isValidEmail(email)) {
            showError(R.string.error_auth_email)
            return
        } else if (!isValidPassword(password)) {
            showError(R.string.error_auth_password)
            return
        }

        FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    when (it.isSuccessful) {
                        true -> {
                            startActivity(Intent(context, MainActivity::class.java))
                            requireActivity().finish()
                        }
                        false -> {
                            val message = getString(R.string.error_auth)
                            showError("${message}\n${it.exception?.localizedMessage}")
                        }
                    }
                }
    }


    private fun showError(@StringRes stringRes: Int) {
        showError(getString(stringRes))
    }

    private fun showError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}