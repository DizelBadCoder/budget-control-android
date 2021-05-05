package dizel.budget_control.budget.domain

import dizel.budget_control.utils.MissingDataException

object BudgetApiToBudgetMapper {
    fun map(budgetApi: BudgetApi): Budget {
        budgetApi.let {
            return Budget(
                title = it.title ?: throw MissingDataException(),
                category = mapCategoryList(it.category),
                currency = getCurrency(it.currency),
                sum = it.sum?.toDoubleOrNull() ?: 0.0
            )
        }
    }

    private fun mapCategoryList(categoryList: List<CategoryApi>?): List<Category> {
        if (categoryList == null) return emptyList()

        return categoryList.map {
            Category(
                id = it.id ?: throw MissingDataException(),
                money = it.money?.toDoubleOrNull() ?: 0.0,
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