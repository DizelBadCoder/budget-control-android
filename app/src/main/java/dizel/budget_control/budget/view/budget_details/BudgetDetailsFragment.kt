package dizel.budget_control.budget.view.budget_details

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import dizel.budget_control.R
import dizel.budget_control.budget.domain.Budget
import dizel.budget_control.databinding.FragmentBudgetDetailsBinding
import dizel.budget_control.utils.ResultRequest
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class BudgetDetailsFragment: Fragment(R.layout.fragment_budget_details) {
    private var _binding: FragmentBudgetDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<BudgetDetailsViewModel>()

    private var categoryAdapter: CategoryListAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentBudgetDetailsBinding.bind(view)

        subscribeUi()
        setUpAdapters()
    }

    private fun subscribeUi() {
        viewModel.budget.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultRequest.Success -> {
                    setUpBudgetDetails(result.data)
                }
                is ResultRequest.Error -> {
                    Timber.e(result.exception)
                    Toast.makeText(context, result.exception.message, Toast.LENGTH_SHORT).show()
                }
                is ResultRequest.Loading -> { }
            }
        }
    }

    private fun setUpAdapters() {
        categoryAdapter = CategoryListAdapter()
        binding.vCategoryList.adapter = categoryAdapter
    }

    private fun setUpBudgetDetails(budget: Budget) {
        with(binding) {
            vNameBudget.text = budget.title
            vTotalSumBudget.text = getString(R.string.total, budget.sum.toString())
        }

        categoryAdapter?.submitList(budget.categoryList)
    }



    companion object {
        const val BUDGET_KEY_DETAILS = "budget_key_details"

        fun newInstance(budgetId: String): Fragment =
            BudgetDetailsFragment().apply {
                arguments = Bundle().apply {
                    putString(BUDGET_KEY_DETAILS, budgetId)
                }
            }
    }
}