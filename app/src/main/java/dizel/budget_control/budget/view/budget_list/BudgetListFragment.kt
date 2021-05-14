package dizel.budget_control.budget.view.budget_list

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.asLiveData
import dizel.budget_control.R
import dizel.budget_control.budget.view.budget_details.BudgetDetailsFragment
import dizel.budget_control.budget.view.create_budget.CreateBudgetFragment
import dizel.budget_control.utils.ResultRequest
import dizel.budget_control.databinding.FragmentListBudgetBinding
import dizel.budget_control.utils.startFragment
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
            vStubButton.setOnClickListener { navigateToCreateBudget() }
            vSwipeRefresher.setOnRefreshListener { viewModel.loadBudgetList() }
        }

        setUpAdapter()
        subscribeUI()
    }

    private fun subscribeUI() {
        viewModel.budgetList.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultRequest.Success -> {
                    budgetListAdapter?.submitList(result.data)
                    binding.vStub.isVisible = result.data.isEmpty()
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

        viewModel.budgetDetailFlow.asLiveData().observe(viewLifecycleOwner) {
            navigateToBudgetDetails(it)
        }
    }

    private fun navigateToBudgetDetails(id: String) {
        val fragment = BudgetDetailsFragment.newInstance(id)
        startFragment(fragment, FRAGMENT_NAME)
    }

    private fun navigateToCreateBudget() {
        val fragment = CreateBudgetFragment()
        startFragment(fragment, FRAGMENT_NAME)
    }

    private fun hideLoadingState() {
        binding.vSwipeRefresher.isRefreshing = false
        binding.vLoadingProgressBar.isVisible = false
    }

    private fun setUpAdapter() {
        budgetListAdapter = BudgetListAdapter(viewModel)
        binding.vRecyclerView.adapter = budgetListAdapter
    }
    companion object {
        const val BUDGET_LIST_KEY = "BUDGET_LIST_BACK_STACK"
        const val FRAGMENT_NAME = "BudgetListFragment"
    }
}

