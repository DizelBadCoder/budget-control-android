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
import dizel.budget_control.core.utils.ResultRequest
import dizel.budget_control.databinding.FragmentListBudgetBinding
import dizel.budget_control.core.utils.startFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

@Composable
fun BudgetListScreen(
    viewModel: BudgetListViewModel = viewModel(),
    navigateToCreateBudget: () -> Unit,
    navigateToBudgetDetails: (MutableList<Char>) -> Unit
) {
    @Composable
    fun BudgetListScaffold(
        topBar: @Composable () -> Unit,
        floatingActionButton: @Composable () -> Unit,
        swipeRefreshState: SwipeRefreshState,
        onRefresh: () -> Unit,
        budgetListContent: @Composable () -> Unit
    ) {
        BudgetListScaffoldContent(
            topBar = topBar,
            floatingActionButton = floatingActionButton,
            swipeRefreshState = swipeRefreshState,
            onRefresh = onRefresh,
            budgetListContent = budgetListContent
        )
    }
}

@Composable
fun BudgetListContent(
    budgetList: ResultRequest<List<Budget>>,
    navigateToBudgetDetails: (MutableList<Char>) -> Unit
) {
    when (val result = budgetList) {
        is ResultRequest.Success -> BudgetList(result.data, navigateToBudgetDetails)
        is ResultRequest.Error -> ErrorStub(result.exception)
        is ResultRequest.Loading -> LoadingState()
    }
}
    val budgetList by viewModel.budgetList.collectAsState(initial = ResultRequest.Loading)

    BudgetListScaffold(
        topBar = { BudgetListTopBar(onLogoutClick = viewModel::signOut) },
        floatingActionButton = { BudgetListFloatingActionButton(onClick = navigateToCreateBudget) },
        swipeRefreshState = rememberSwipeRefreshState(isRefreshing = budgetList is ResultRequest.Loading),
        onRefresh = viewModel::loadBudgetList,
        budgetListContent = { BudgetListContent(budgetList, navigateToBudgetDetails) }
    )
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

    // No equivalent in Compose, as the adapter is replaced with a LazyColumn in the BudgetList Composable.

    // No equivalent in Compose, as the Toolbar is replaced with a TopAppBar in the BudgetListScreen Composable.

    // No equivalent in Compose, as the sign out logic is moved to the BudgetListTopBar Composable.

    override fun onResume() {
        super.onResume()
        viewModel.loadBudgetList()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.budget_list_menu, menu)
    }

    companion object {
        const val BUDGET_LIST_KEY = "BUDGET_LIST_BACK_STACK"
        const val FRAGMENT_NAME = "BudgetListFragment"
    }
}

