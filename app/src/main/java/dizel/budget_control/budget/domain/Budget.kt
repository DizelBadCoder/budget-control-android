package dizel.budget_control.budget.domain

data class Budget(
    val sum: Long,
    val categoryList: List<Category>,
    val currency: Currency,
    val title: String
)

data class Category(
    val id: BudgetId,
    val name: String,
    val money: Long,
    val currency: Currency,
    val color: String
) {
    companion object {
        const val AVAILABLE_MONEY_KEY = "AvailableMoney"
        const val DEFAULT_COLOR = "#e4e4e4"
    }
}

typealias BudgetId = String

enum class Currency(val symbol: String) {
    RUB("₽"),
    USD("$")
}