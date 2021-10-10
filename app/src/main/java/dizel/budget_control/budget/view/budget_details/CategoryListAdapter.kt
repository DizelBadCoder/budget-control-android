package dizel.budget_control.budget.view.budget_details

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dizel.budget_control.R
import dizel.budget_control.budget.domain.entity.Category
import dizel.budget_control.databinding.ItemCategoryBinding
import dizel.budget_control.core.extensions.toMoneyMask

class CategoryListAdapter(
   private val viewModel: BudgetDetailsViewModel
): ListAdapter<Category, CategoryListAdapter.CategoryViewHolder>(CategoryDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_category, parent, false)
        val binding = ItemCategoryBinding.bind(view)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    override fun getItemCount() = currentList.size

    inner class CategoryViewHolder(
            private val binding: ItemCategoryBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(category: Category) = with (binding) {
            vCategoryName.text = category.name
            vCategoryMoney.text = category.money.toMoneyMask(category.currency)
            vCategoryColor.setBackgroundColor(Color.parseColor(category.color))

            if (category.id == Category.AVAILABLE_MONEY_KEY) {
                vDeleteCategoryButton.isVisible = false
            }

            vDeleteCategoryButton.setOnClickListener {
                viewModel.removeCategory(category.id)
            }
        }
    }
}


private class CategoryDiffUtil : DiffUtil.ItemCallback<Category>() {
    override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem == newItem
    }
}