package dizel.budget_control.auth.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dizel.budget_control.R
import dizel.budget_control.utils.replaceFragment

class AuthActivity : AppCompatActivity(R.layout.activity_auth) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        actionBar?.hide()
        replaceFragment(SplashAuthFragment())
    }
}