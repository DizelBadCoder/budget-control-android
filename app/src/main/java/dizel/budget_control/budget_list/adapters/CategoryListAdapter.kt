package dizel.budget_control.budget_list.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dizel.budget_control.R
import dizel.budget_control.budget_list.Category
import dizel.budget_control.databinding.ItemCategoryBinding

class CategoryListAdapter: RecyclerView.Adapter<CategoryListAdapter.CategoryViewHolder>() {

    private val categoryList = ArrayList<Category>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(categoryList[position])
    }

    override fun getItemCount() = categoryList.size

    inner class CategoryViewHolder(
            private val view: View
    ): RecyclerView.ViewHolder(view) {
        @SuppressLint("SetTextI18n")
        fun bind(category: Category) {
            ItemCategoryBinding.bind(view).apply {
                vCategoryName.text = category.name
                vCategoryMoney.text = "${category.money} ${category.currency.symbol}"
                vCategoryColor.setBackgroundColor(Color.parseColor(category.color))
            }
        }
    }
}