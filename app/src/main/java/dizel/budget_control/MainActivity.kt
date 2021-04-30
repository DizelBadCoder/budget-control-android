package dizel.budget_control

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import dizel.budget_control.auth.view.AuthActivity
import dizel.budget_control.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = FirebaseAuth.getInstance().currentUser
        binding.apply {
            vText.text = user?.uid
            vSignOutButton.setOnClickListener { signOutUser() }
        }
    }

    private fun signOutUser() {
        FirebaseAuth.getInstance().signOut()

        startActivity(Intent(this, AuthActivity::class.java))
        finish()
    }
}