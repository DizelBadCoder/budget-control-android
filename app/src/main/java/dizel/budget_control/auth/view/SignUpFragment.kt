package dizel.budget_control.auth.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import dizel.budget_control.budget.view.MainActivity
import dizel.budget_control.R
import dizel.budget_control.databinding.FragmentSingUpBinding
import dizel.budget_control.utils.isValidEmail
import dizel.budget_control.utils.isValidPassword

class SignUpFragment: Fragment(R.layout.fragment_sing_up) {
    private var _binding: FragmentSingUpBinding? = null
    private val binding get() = _binding!!


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentSingUpBinding.bind(view).apply {
            vSignUpButton.setOnClickListener { signUpUser() }
        }

        setUpToolbar()
    }

    private fun signUpUser() {
        val email = binding.vEditTextEmail.text.toString()
        val password = binding.vEditTextPassword.text.toString()
        val passwordConfirm = binding.vEditTextConfirmPassword.text.toString()

        if (!isValidEmail(email)) {
            showError(R.string.error_auth_email)
            return
        } else if (!isValidPassword(password)) {
            showError(R.string.error_auth_password)
            return
        } else if (password != passwordConfirm) {
            showError(R.string.error_auth_confirm_password)
            return
        }

        FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email, password)
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

    private fun setUpToolbar() {
        with (binding.vToolBar) {
            (requireActivity() as AppCompatActivity).setSupportActionBar(this)
            setNavigationIcon(R.drawable.ic_arrow_back)
            setNavigationOnClickListener {
                requireActivity().onBackPressed()
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