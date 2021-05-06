package dizel.budget_control.budget.view.budget_details

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import dizel.budget_control.databinding.FragmentBudgetDetailsBinding

class BudgetDetailsFragment: Fragment() {
    private var _binding: FragmentBudgetDetailsBinding? = null
    private val binding get() = _binding!!


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentBudgetDetailsBinding.bind(view).apply {

        }

    }

    companion object {
        fun newInstance(budgetId: String): BudgetDetailsFragment {
            val fragment = BudgetDetailsFragment()
            val bundle = Bundle()

            bundle.putString(BUDGET_KEY, budgetId)
            fragment.arguments = bundle
            return fragment
        }

        private const val BUDGET_KEY = "budget_key"
    }
}