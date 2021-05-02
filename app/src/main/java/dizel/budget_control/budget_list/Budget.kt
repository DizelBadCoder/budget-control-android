package dizel.budget_control.budget_list

data class Budget(
        val sum: Double,
        val category: List<Category>,
        val currency: Currency,
        val title: String
)

data class Category(
        val id: String,
        val name: String,
        val money: Double,
        val currency: Currency,
        val color: String
)

enum class Currency(val symbol: String) {
    RUB("₽"),
    USD("$")
}