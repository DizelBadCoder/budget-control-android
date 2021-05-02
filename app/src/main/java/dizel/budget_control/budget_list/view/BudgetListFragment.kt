package dizel.budget_control.budget_list.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import dizel.budget_control.R
import dizel.budget_control.budget_list.adapters.BudgetListAdapter
import dizel.budget_control.databinding.FragmentListBudgetBinding
import dizel.budget_control.firebase.DatabaseHelper

class BudgetListFragment: Fragment(R.layout.fragment_list_budget) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentListBudgetBinding.bind(view).apply {
            vFloatingButton.setOnClickListener { goToCreateBudgetFragment() }
        }

        val budgetAdapter = BudgetListAdapter()
        val budgets = DatabaseHelper().getAllBudgets()

        budgetAdapter.updateList(budgets)
        binding.vRecyclerView.adapter = budgetAdapter

    }

    private fun goToCreateBudgetFragment() {
        (fragmentManager ?: return)
            .beginTransaction()
            .replace(R.id.vFragmentContainer, CreateBudgetFragment())
            .addToBackStack(null)
            .commit()
    }
}

