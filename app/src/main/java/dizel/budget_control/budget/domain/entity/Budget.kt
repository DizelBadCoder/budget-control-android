package dizel.budget_control.budget.domain.entity

import dizel.budget_control.budget.domain.mappers.BudgetToHashMapMapper

data class Budget(
    val id: String,
    val sum: Double,
    val categoryList: List<Category>,
    val currency: Currency,
    val title: String
) {
    fun toHashMap(): HashMap<String, Any> {
        return hashMapOf(
            "title" to title,
            "budgetSum" to sum,
            "currency" to currency.name,
            "category" to BudgetToHashMapMapper.mapCategoryListToHashMap(categoryList)
        )
    }
}

data class Category(
    val id: String,
    val name: String,
    val money: Double,
    val currency: Currency,
    val color: String
) {

    fun toHashMap(): HashMap<String, Any> {
        return hashMapOf(
            "categoryId" to id,
            "categoryName" to name,
            "categoryMoney" to money,
            "categoryCurrency" to currency.name,
            "categoryColor" to color
        )
    }

    companion object {
        const val AVAILABLE_MONEY_KEY = "AvailableMoney"
        const val DEFAULT_COLOR = "#e4e4e4"

        const val PATH_TO_AVAILABLE_MONEY_CATEGORY = "category/$AVAILABLE_MONEY_KEY/categoryMoney"
    }
}

enum class Currency (
    val symbol: String,
    val cost: Double
) {
    RUB("₽", 1.0),
    USD("$", 74.0)
}