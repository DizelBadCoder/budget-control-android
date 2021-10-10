package dizel.budget_control.auth.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import dizel.budget_control.R
import dizel.budget_control.databinding.FragmentSplashAuthBinding
import dizel.budget_control.core.utils.startFragment

class SplashAuthFragment: Fragment(R.layout.fragment_splash_auth) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        FragmentSplashAuthBinding.bind(view).apply {
            vSignInButton.setOnClickListener { startFragment(SignInFragment()) }
            vSignUpButton.setOnClickListener { startFragment(SignUpFragment()) }
        }
    }
}