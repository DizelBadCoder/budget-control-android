package dizel.budget_control.budget_list.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dizel.budget_control.R
import dizel.budget_control.budget_list.Budget
import dizel.budget_control.databinding.ViewBudgetBinding

class BudgetListAdapter: RecyclerView.Adapter<BudgetListAdapter.BudgetViewHolder>() {

    private val budgetList = ArrayList<Budget>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BudgetViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.view_budget, parent, false)
        val binding = ViewBudgetBinding.bind(view)
        return BudgetViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BudgetViewHolder, position: Int) {
        holder.bind(budgetList[position])
    }

    override fun getItemCount() = budgetList.size

    fun updateList(list: List<Budget>) {
        budgetList.clear()
        budgetList.addAll(list)
        notifyDataSetChanged()
    }

    inner class BudgetViewHolder(
            private val binding: ViewBudgetBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(budget: Budget) = with (binding) {
            val reminder = budget.sum - budget.category.map { it.money }.sum()
            val symbol = " " + budget.currency.symbol

            vNameBudget.text = budget.title
            vTotalSumBudget.text = budget.sum.toString() + symbol
            vRemainderBudget.text = reminder.toString() + symbol
        }
    }
}