package dizel.budget_control.budget.mappers

import dizel.budget_control.budget.domain.*
import dizel.budget_control.utils.MissingDataException

class BudgetApiToBudgetMapper(
    private val categoryList: List<CategoryApi>
) {
    fun map(budgetApi: BudgetApi): Budget {
        budgetApi.let {
            return Budget(
                title = it.title ?: "throw MissingDataException()",
                categoryList = mapCategoryList(categoryList),
                currency = getCurrency(it.currency),
                sum = it.sum ?: 0
            )
        }
    }

    private fun mapCategoryList(
        categoryList: List<CategoryApi>
    ): List<Category> {
        return categoryList.map {
            Category(
                id = it.id ?: "throw MissingDataException()",
                money = it.money ?: 0,
                name = it.name ?: "Empty name",
                currency = getCurrency(it.currency),
                color = it.color ?: "#FFFFFF"
            )
        }
    }

    private fun getCurrency(currency: String?) = try {
        Currency.valueOf(currency.orEmpty())
    } catch (ex: IllegalArgumentException) {
        Currency.values().first()
    }

}