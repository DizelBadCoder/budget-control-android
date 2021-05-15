package dizel.budget_control.budget.view.budget_list

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import com.google.firebase.auth.FirebaseAuth
import dizel.budget_control.R
import dizel.budget_control.auth.view.AuthActivity
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

        setHasOptionsMenu(true)

        setUpToolbar()
        setUpAdapter()
        subscribeUI()
    }

    private fun subscribeUI() {
        viewModel.budgetList.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultRequest.Success -> {
                    budgetListAdapter?.submitList(result.data)
                    when (result.data.isEmpty()) {
                        true -> showEmptyListStub()
                        false -> hideStub()
                    }
                    hideLoadingState()
                }
                is ResultRequest.Error -> {
                    Timber.e(result.exception)
                    showErrorStub(result.exception)
                    hideLoadingState()
                }
                is ResultRequest.Loading -> { }
            }
        }

        viewModel.budgetDetailFlow.asLiveData().observe(viewLifecycleOwner) {
            navigateToBudgetDetails(it)
        }
    }

    private fun showErrorStub(throwable: Throwable) {
        with (binding) {
            vStub.isVisible = true
            vStubButton.isVisible = false
            vStubTitle.setText(R.string.error_stub_title)
            vStubMessage.text = throwable.message
        }
    }

    private fun showEmptyListStub() {
        with (binding) {
            vStub.isVisible = true
            vStubButton.isVisible = true
            vStubTitle.setText(R.string.budgets_list_is_empty_stub_title)
            vStubMessage.setText(R.string.budgets_list_is_empty_stub_message)
        }
    }

    private fun hideStub() {
        binding.vStub.isVisible = false
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

    private fun setUpToolbar() {
        with (binding.vToolBar) {
            (requireActivity() as AppCompatActivity).setSupportActionBar(this)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menu_logout -> singOut()
                }
                true
            }
        }
    }

    private fun singOut() {
        FirebaseAuth.getInstance().signOut()

        startActivity(Intent(context, AuthActivity::class.java))
        (requireActivity() as AppCompatActivity).finish()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.budget_list_menu, menu)
    }

    companion object {
        const val BUDGET_LIST_KEY = "BUDGET_LIST_BACK_STACK"
        const val FRAGMENT_NAME = "BudgetListFragment"
    }
}

