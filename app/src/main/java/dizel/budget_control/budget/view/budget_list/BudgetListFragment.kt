package dizel.budget_control.budget.view.budget_list

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import dizel.budget_control.R
import dizel.budget_control.budget.view.create_budget.CreateBudgetFragment
import dizel.budget_control.utils.ResultRequest
import dizel.budget_control.databinding.FragmentListBudgetBinding
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
            vFloatingButton.setOnClickListener { goToCreateBudgetFragment() }
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
                }
                is ResultRequest.Error -> {
                    Timber.e(result.exception)
                    Toast.makeText(context, result.exception.message, Toast.LENGTH_SHORT).show()
                }
                is ResultRequest.Loading -> {
                    // TODO show loading view
                }
            }
        }
    }

    private fun setUpAdapter() {
        budgetListAdapter = BudgetListAdapter()
        binding.vRecyclerView.adapter = budgetListAdapter
    }

    private fun setUpToolbar() {
        activity?.actionBar?.setTitle(R.string.list_budgets)
    }

    private fun goToCreateBudgetFragment() {
        parentFragmentManager
            .beginTransaction()
            .replace(R.id.vFragmentContainer, CreateBudgetFragment())
            .addToBackStack(null)
            .commit()
    }
}

