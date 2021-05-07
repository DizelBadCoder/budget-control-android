package dizel.budget_control.budget.view.budget_list

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import dizel.budget_control.R
import dizel.budget_control.budget.domain.BudgetId
import dizel.budget_control.budget.view.budget_details.BudgetDetailsFragment
import dizel.budget_control.budget.view.create_budget.CreateBudgetFragment
import dizel.budget_control.utils.ResultRequest
import dizel.budget_control.databinding.FragmentListBudgetBinding
import dizel.budget_control.utils.replaceFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class BudgetListFragment: Fragment(R.layout.fragment_list_budget) {
    private var _binding: FragmentListBudgetBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<BudgetListViewModel>()

    private var budgetListAdapter: BudgetListAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentListBudgetBinding.bind(view).apply {
            vFloatingButton.setOnClickListener { navigateToCreateBudget() }
            vSwipeRefresher.setOnRefreshListener { viewModel.loadBudgetList() }
        }

        setUpAdapter()
        setUpToolbar()
        subscribeUI()
    }

    private fun subscribeUI() {
        viewModel.budgetList.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultRequest.Success -> {
                    budgetListAdapter?.submitList(result.data)
                    hideLoadingState()
                }
                is ResultRequest.Error -> {
                    Timber.e(result.exception)
                    Toast.makeText(context, result.exception.message, Toast.LENGTH_SHORT).show()
                    hideLoadingState()
                }
                is ResultRequest.Loading -> { }
            }
        }

    }

    private fun navigateToBudgetDetails(id: BudgetId) {
        val fragment = BudgetDetailsFragment.newInstance(id)
        replaceFragment(fragment)
    }

    private fun navigateToCreateBudget() {
        val fragment = CreateBudgetFragment()
        replaceFragment(fragment)
    }

    private fun hideLoadingState() {
        binding.vSwipeRefresher.isRefreshing = false
        binding.vLoadingProgressBar.isVisible = false
    }

    private fun setUpAdapter() {
        budgetListAdapter = BudgetListAdapter()
        binding.vRecyclerView.adapter = budgetListAdapter
    }

    private fun setUpToolbar() {
        activity?.actionBar?.setTitle(R.string.list_budgets)
    }
}

