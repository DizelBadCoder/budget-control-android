package dizel.budget_control

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import dizel.budget_control.auth.view.AuthActivity
import dizel.budget_control.budget_list.view.MainActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val user = FirebaseAuth.getInstance().currentUser
        val intent = when (user != null) {
            true -> Intent(this, MainActivity::class.java)
            false -> Intent(this, AuthActivity::class.java)
        }
        startActivity(intent)
        finish()
    }
}