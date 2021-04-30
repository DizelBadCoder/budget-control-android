package dizel.budget_control.auth.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import dizel.budget_control.R
import dizel.budget_control.databinding.FragmentSplashAuthBinding

class SplashAuthFragment: Fragment(R.layout.fragment_splash_auth) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        FragmentSplashAuthBinding.bind(view).apply {
            vSignInButton.setOnClickListener { replaceFragment(SignInFragment()) }
            vSignUpButton.setOnClickListener { replaceFragment(SignUpFragment()) }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        fragmentManager
                ?.beginTransaction()
                ?.replace(R.id.vFragmentContainer, fragment)
                ?.addToBackStack(null)
                ?.commit()
    }
}