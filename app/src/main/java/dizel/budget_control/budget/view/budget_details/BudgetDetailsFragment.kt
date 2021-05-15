package dizel.budget_control.budget.view.budget_details

import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import dizel.budget_control.R
import dizel.budget_control.budget.domain.Budget
import dizel.budget_control.budget.view.budget_list.BudgetListFragment
import dizel.budget_control.budget.view.create_category.CreateCategoryFragment
import dizel.budget_control.databinding.FragmentBudgetDetailsBinding
import dizel.budget_control.utils.MissingDataException
import dizel.budget_control.utils.ResultRequest
import dizel.budget_control.utils.replaceFragment
import dizel.budget_control.utils.startFragment
import lecho.lib.hellocharts.model.PieChartData
import lecho.lib.hellocharts.model.SliceValue
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class BudgetDetailsFragment: Fragment(R.layout.fragment_budget_details) {
    private var _binding: FragmentBudgetDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<BudgetDetailsViewModel>()

    private var categoryAdapter: CategoryListAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentBudgetDetailsBinding.bind(view).apply {
            vSwipeRefresher.setOnRefreshListener { viewModel.retry() }
            vCreateCategoryButton.setOnClickListener { navigateToCreateCategory() }
        }

        setHasOptionsMenu(true)
        setUpToolbar()
        loadBudgetFromArguments()
        subscribeUi()
        setUpAdapters()
    }

    private fun subscribeUi() {
        viewModel.budget.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultRequest.Success -> {
                    setUpBudgetDetails(result.data)
                    hideLoadingBar()
                }
                is ResultRequest.Error -> {
                    Timber.e(result.exception)
                    Toast.makeText(context, result.exception.message, Toast.LENGTH_SHORT).show()
                    hideLoadingBar()
                }
                is ResultRequest.Loading -> { }
            }
        }
    }

    private fun loadBudgetFromArguments() {
        arguments?.getString(BUDGET_KEY_DETAILS).let {
            viewModel.loadBudgetById(
                budgetId = it ?: throw MissingDataException()
            )
        }
    }

    private fun hideLoadingBar() {
        binding.vSwipeRefresher.isRefreshing = false
    }

    private fun setUpAdapters() {
        categoryAdapter = CategoryListAdapter()
        binding.vCategoryList.adapter = categoryAdapter
    }

    private fun setUpBudgetDetails(budget: Budget) {
        with(binding) {
            vNameBudget.text = budget.title
            vTotalSumBudget.text = getString(R.string.total, budget.sum.toString())

            vChart.pieChartData = PieChartData().apply {
                values = budget.categoryList.map {
                    SliceValue(
                        it.money.toFloat(),
                        Color.parseColor(it.color)
                    )
                }
                setHasCenterCircle(true)
            }
            vChart.isInteractive = false
        }

        categoryAdapter?.submitList(budget.categoryList)
    }

    private fun setUpToolbar() {
        with(binding.vToolBar) {
            (requireActivity() as AppCompatActivity).setSupportActionBar(this)
            setNavigationIcon(R.drawable.ic_arrow_back)
            setNavigationOnClickListener {
                val fragment = BudgetListFragment()
                replaceFragment(fragment)
            }

            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menu_delete_budget -> deleteThisBudget()
                }
                true
            }
        }
    }

    private fun deleteThisBudget() {
        viewModel.deleteBudget().observe(viewLifecycleOwner) {
            when (it) {
                is ResultRequest.Success -> {
                    val fragment = BudgetListFragment()
                    replaceFragment(fragment)
                }
                is ResultRequest.Error -> {
                    Timber.e(it.exception)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.budget_details_menu, menu)
    }

    private fun navigateToCreateCategory() {
        val fragment = CreateCategoryFragment()
        startFragment(fragment)
    }

    companion object {
        const val BUDGET_KEY_DETAILS = "budget_key_details"
        const val FRAGMENT_NAME = "BudgetDetailsFragment"

        fun newInstance(budgetId: String): Fragment =
            BudgetDetailsFragment().apply {
                arguments = Bundle().apply {
                    putString(BUDGET_KEY_DETAILS, budgetId)
                }
            }
    }
}