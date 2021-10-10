package dizel.budget_control.app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import dizel.budget_control.auth.view.AuthActivity
import dizel.budget_control.budget.view.MainActivity
import org.koin.android.ext.android.inject

class SplashActivity : AppCompatActivity() {

    private val authFirebase by inject<FirebaseAuth>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val userIsLogin = authFirebase.currentUser != null

        val intent = when (userIsLogin) {
            true -> Intent(this, MainActivity::class.java)
            false -> Intent(this, AuthActivity::class.java)
        }
        startActivity(intent)
        finish()
    }
}