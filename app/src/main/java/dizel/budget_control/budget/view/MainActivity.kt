package dizel.budget_control.budget.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dizel.budget_control.R
import dizel.budget_control.budget.view.budget_list.BudgetListFragment
import dizel.budget_control.core.utils.replaceFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BudgetControlTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    BudgetListScreen()
                }
            // Navigation logic is now handled in the Composable function BudgetListScreen