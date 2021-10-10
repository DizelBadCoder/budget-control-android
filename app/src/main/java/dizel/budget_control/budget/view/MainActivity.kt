package dizel.budget_control.budget.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dizel.budget_control.R
import dizel.budget_control.budget.view.budget_list.BudgetListFragment
import dizel.budget_control.core.utils.replaceFragment

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpFragmentContainer()
    }

    private fun setUpFragmentContainer() {
        val fragment = BudgetListFragment()
        replaceFragment(fragment)
    }
}