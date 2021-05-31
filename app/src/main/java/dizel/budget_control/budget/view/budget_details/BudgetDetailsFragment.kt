package dizel.budget_control.budget.view.budget_details

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import dizel.budget_control.R
import dizel.budget_control.budget.domain.Budget
import dizel.budget_control.budget.view.budget_list.BudgetListFragment
import dizel.budget_control.budget.view.create_category.CreateCategoryFragment
import dizel.budget_control.databinding.FragmentBudgetDetailsBinding
import dizel.budget_control.utils.*
import lecho.lib.hellocharts.model.PieChartData
import lecho.lib.hellocharts.model.SliceValue
import org.koin.androidx.viewmodel.ext.android.viewModel

class BudgetDetailsFragment: Fragment(R.layout.fragment_budget_details) {
    private var _binding: FragmentBudgetDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<BudgetDetailsViewModel>()

    private var budgetId: String? = null
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
        viewModel.budget.observe(viewLifecycleOwner) { budget ->
            setUpBudgetDetails(budget)
            hideLoadingBar()
        }
    }

    private fun loadBudgetFromArguments() {
        arguments?.getString(BUDGET_KEY_DETAILS).let {
            budgetId = it
            viewModel.loadBudgetById(
                budgetId = it ?: throw MissingDataException()
            )
        }
    }

    private fun hideLoadingBar() {
        binding.vSwipeRefresher.isRefreshing = false
    }

    private fun setUpAdapters() {
        categoryAdapter = CategoryListAdapter(viewModel)
        binding.vCategoryList.adapter = categoryAdapter
    }

    private fun setUpBudgetDetails(budget: Budget) {
        with(binding) {
            vNameBudget.text = budget.title

            val total = budget.sum.toMoneyMask(budget.currency)
            vTotalSumBudget.text = getString(R.string.total, total)

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
                parentFragmentManager.popBackStack()
            }

            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menu_delete_budget -> askToDeleteBudget()
                    R.id.menu_rename_budget -> askToRenameBudget()
                }
                true
            }
        }
    }

    private fun askToRenameBudget() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.rename_budget)
            .setView(R.layout.dialog_rename_budget)
            .create()
            .apply {
                setButton(DialogInterface.BUTTON_POSITIVE, "Yes") { _, _ ->
                    val text = findViewById<EditText>(R.id.vEditText)?.text.toString()
                    viewModel.renameBudget(text)
                }

                setButton(DialogInterface.BUTTON_NEGATIVE, "No") { _, _ -> dismiss() }

                show()
            }
    }

    private fun askToDeleteBudget() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.delete_budget)
            .setMessage(R.string.ask_you_are_sure)
            .setPositiveButton("Yes") { _, _ -> deleteThisBudget() }
            .setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun deleteThisBudget() {
        viewModel.deleteBudget().observe(viewLifecycleOwner) {
            when (it) {
                is ResultRequest.Success -> {
                    val fragment = BudgetListFragment()
                    replaceFragment(fragment)
                }
                else -> { }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.budget_details_menu, menu)
    }

    override fun onResume() {
        super.onResume()
        viewModel.retry()
    }

    private fun navigateToCreateCategory() {
        val fragment = CreateCategoryFragment.newInstance(budgetId!!)
        startFragment(fragment)
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