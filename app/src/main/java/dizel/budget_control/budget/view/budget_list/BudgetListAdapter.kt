package dizel.budget_control.budget.view.budget_list

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dizel.budget_control.R
import dizel.budget_control.budget.domain.entity.Budget
import dizel.budget_control.budget.domain.entity.Category
import dizel.budget_control.databinding.ViewBudgetBinding
import dizel.budget_control.core.extensions.toMoneyMask
import lecho.lib.hellocharts.model.PieChartData
import lecho.lib.hellocharts.model.SliceValue

class BudgetListAdapter(
    private val viewModel: BudgetListViewModel
): ListAdapter<Budget, BudgetListAdapter.BudgetViewHolder>(BudgetDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BudgetViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.view_budget, parent, false)
        val binding = ViewBudgetBinding.bind(view)
        return BudgetViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BudgetViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    override fun getItemCount() = currentList.size

    inner class BudgetViewHolder(
            private val binding: ViewBudgetBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(budget: Budget) = with (binding) {
            val context = this@BudgetViewHolder.itemView.context
            val available = budget.categoryList.find { it.id == Category.AVAILABLE_MONEY_KEY }

            val remainder = available?.money?.toMoneyMask(budget.currency)
            val total = budget.sum.toMoneyMask(budget.currency)

            vNameBudget.text = budget.title
            vTotalSumBudget.text = context.getString(R.string.total, total)
            vRemainderBudget.text = context.getString(R.string.remainder, remainder)

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

            binding.root.setOnClickListener {
                viewModel.navigateToBudgetDetail(budget.id)
            }
        }
    }
}

private class BudgetDiffUtil : DiffUtil.ItemCallback<Budget>() {
    override fun areItemsTheSame(oldItem: Budget, newItem: Budget): Boolean {
        return oldItem.title == newItem.title
    }

    override fun areContentsTheSame(oldItem: Budget, newItem: Budget): Boolean {
        return oldItem == newItem
    }
}