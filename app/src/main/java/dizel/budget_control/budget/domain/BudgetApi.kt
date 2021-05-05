package dizel.budget_control.budget.domain

data class BudgetApi(
    val sum: String? = null,
    val category: List<CategoryApi>? = null,
    val currency: String? = null,
    val title: String? = null
)

data class CategoryApi(
    val id: String? = null,
    val name: String? = null,
    val money: String? = null,
    val currency: String? = null,
    val color: String? = null
)