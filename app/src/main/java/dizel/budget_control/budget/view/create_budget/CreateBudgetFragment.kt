package dizel.budget_control.budget.view.create_budget

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import dizel.budget_control.R
import dizel.budget_control.budget.domain.Budget
import dizel.budget_control.budget.domain.Currency
import dizel.budget_control.databinding.FragmentCreateBudgetBinding

class CreateBudgetFragment: Fragment(R.layout.fragment_create_budget) {
    private var _binding: FragmentCreateBudgetBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCreateBudgetBinding.bind(view).apply {
            vSubmitButton.setOnClickListener { createNewBudgetAndGoIn() }

            vSpinnerCurrency.adapter = ArrayAdapter(
                            view.context,
                            android.R.layout.simple_spinner_item,
                            Currency.values().map { "${it.symbol} ${it.name}" }
                    )
        }

        setUpToolbar()
    }

    private fun createNewBudgetAndGoIn() {
        val budget = getBudget()

    }

    private fun getBudget() = Budget(
        title = binding.vNameBudget.text.toString(),
        sum = binding.vMoneyBudget.text.toString().toDouble(),
        currency = Currency.valueOf(binding.vSpinnerCurrency.selectedItem as String),
        category = emptyList()
    )

    private fun setUpToolbar() {
        activity?.actionBar?.setTitle(R.string.create_budget)
    }
}