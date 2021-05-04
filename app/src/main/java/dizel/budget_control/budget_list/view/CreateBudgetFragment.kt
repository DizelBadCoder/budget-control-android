package dizel.budget_control.budget_list.view

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import androidx.fragment.app.Fragment
import dizel.budget_control.R
import dizel.budget_control.budget_list.Budget
import dizel.budget_control.budget_list.Currency
import dizel.budget_control.databinding.FragmentCreateBudgetBinding

class CreateBudgetFragment: Fragment(R.layout.fragment_create_budget) {
    private var _binding: FragmentCreateBudgetBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCreateBudgetBinding.bind(view).apply {
            vSubmitButton.setOnClickListener { createBudget() }

            vSpinnerCurrency.adapter = ArrayAdapter(
                            view.context,
                            android.R.layout.simple_spinner_item,
                            Currency.values().map { "${it.symbol} ${it.name}" }
                    )
        }
    }

    private fun createBudget() {
        val budget = Budget(
                title = binding.vNameBudget.text.toString(),
                sum = binding.vMoneyBudget.text.toString().toDouble(),
                currency = Currency.valueOf(binding.vSpinnerCurrency.selectedItem as String),
                category = emptyList()
        )

        // TODO add new budget to firebase
        // TODO go to new budget details screen
    }
}