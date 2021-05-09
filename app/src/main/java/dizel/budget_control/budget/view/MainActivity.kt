package dizel.budget_control.budget.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import dizel.budget_control.R
import dizel.budget_control.auth.view.AuthActivity
import dizel.budget_control.budget.view.budget_list.BudgetListFragment
import dizel.budget_control.utils.replaceFragment

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpFragmentContainer()
    }

    private fun setUpFragmentContainer() {
        val fragment = BudgetListFragment()
        replaceFragment(fragment)
    }

    private fun signOutUser() {
        FirebaseAuth.getInstance().signOut()

        startActivity(Intent(this, AuthActivity::class.java))
        finish()
    }
}