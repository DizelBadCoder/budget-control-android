package dizel.budget_control.budget.view.create_budget

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import dizel.budget_control.R
import dizel.budget_control.budget.domain.Budget
import dizel.budget_control.budget.domain.Category
import dizel.budget_control.budget.domain.Currency
import dizel.budget_control.budget.view.budget_details.BudgetDetailsFragment
import dizel.budget_control.databinding.FragmentCreateBudgetBinding
import dizel.budget_control.utils.ResultRequest
import dizel.budget_control.utils.replaceFragmentWithoutBackStack
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreateBudgetFragment: Fragment(R.layout.fragment_create_budget) {
    private var _binding: FragmentCreateBudgetBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<CreateBudgetViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCreateBudgetBinding.bind(view).apply {
            vSubmitButton.setOnClickListener { createBudget() }

            val adapter = ArrayAdapter(
                view.context,
                android.R.layout.simple_spinner_item,
                Currency.values().map { "${it.name} - ${it.symbol}" }
            )
            vSpinnerCurrency.adapter = adapter
        }

        subscribeUI()
        setUpToolbar()
    }

    private fun subscribeUI() {
        viewModel.createBudgetFlow.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultRequest.Success -> {
                    val id = result.data
                    goToBudgetDetailsFragment(id)
                }
                is ResultRequest.Error -> {
                    showError(result.exception.message ?: getString(R.string.unknown_error))
                }
                is ResultRequest.Loading -> { }
            }
        }
    }

    private fun goToBudgetDetailsFragment(id: String) {
        val fragment = BudgetDetailsFragment.newInstance(id)
        replaceFragmentWithoutBackStack(fragment)
    }

    private fun createBudget() {
        val title = binding.vNameBudget.text.toString().ifEmpty { null }
        val sum = binding.vMoneyBudget.text.toString().toLongOrNull()

        if (title == null || sum == null) {
            showError(getString(R.string.invalidate_fields))
            return
        }

        val currency = Currency.values()[binding.vSpinnerCurrency.selectedItemPosition]

        val categoryList = listOf(
            Category(
                id = Category.AVAILABLE_MONEY_KEY,
                name = Category.AVAILABLE_MONEY_KEY,
                color = Category.DEFAULT_COLOR,
                money = sum,
                currency = currency
            )
        )

        val budget = Budget(
            title = title,
            sum = sum,
            currency = currency,
            categoryList = categoryList
        )
        viewModel.createBudget(budget)
    }

    private fun showError(mes: String) {
        Toast.makeText(context, mes, Toast.LENGTH_LONG).show()
    }

    private fun setUpToolbar() {
        activity?.actionBar?.setTitle(R.string.create_budget)
    }
}