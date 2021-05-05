package dizel.budget_control.budget.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import dizel.budget_control.R
import dizel.budget_control.auth.view.AuthActivity
import dizel.budget_control.budget.budget_list.BudgetListFragment

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpFragmentContainer()
    }

    private fun setUpFragmentContainer() {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.vFragmentContainer, BudgetListFragment())
                .commit()
    }

    private fun signOutUser() {
        FirebaseAuth.getInstance().signOut()

        startActivity(Intent(this, AuthActivity::class.java))
        finish()
    }
}