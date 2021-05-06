package dizel.budget_control.budget.view.budget_list

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dizel.budget_control.R
import dizel.budget_control.budget.domain.Budget
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
            val remainder = budget.sum - budget.categoryList.map { it.money }.sum()
            val symbol = " " + budget.currency.symbol
            val context = this@BudgetViewHolder.itemView.context

            vNameBudget.text = budget.title
            vTotalSumBudget.text =
                context.getString(R.string.total, budget.sum.toString() + symbol)
            vRemainderBudget.text =
                context.getString(R.string.remainder, remainder.toString() + symbol)
        }
    }
}