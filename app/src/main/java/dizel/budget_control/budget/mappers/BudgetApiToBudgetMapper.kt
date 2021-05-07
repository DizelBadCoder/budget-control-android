package dizel.budget_control.budget.mappers

import dizel.budget_control.budget.domain.*
import dizel.budget_control.utils.MissingDataException

class BudgetApiToBudgetMapper(
    private val categoryList: List<CategoryApi>
) {
    fun map(budgetApi: BudgetApi): Budget {
        budgetApi.let { api ->
            return Budget(
                title = api.title ?: throw MissingDataException(),
                categoryList = mapCategoryList(categoryList),
                currency = Currency.valueOf(api.currency.orEmpty()),
                remainder = api.sum ?: 0 - categoryList.map { it.money ?: 0 }.sum(),
                sum = api.sum ?: 0
            )
        }
    }

    private fun mapCategoryList(
        categoryList: List<CategoryApi>
    ): List<Category> {
        return categoryList.map { api ->
            Category(
                id = api.id ?: throw MissingDataException(),
                money = api.money ?: 0,
                name = api.name ?: "Empty name",
                currency = Currency.valueOf(api.currency.orEmpty()),
                color = api.color ?: "#FFFFFF"
            )
        }
    }
}