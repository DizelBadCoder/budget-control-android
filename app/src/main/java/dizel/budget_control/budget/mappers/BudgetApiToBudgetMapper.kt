package dizel.budget_control.budget.mappers

import dizel.budget_control.budget.domain.*
import dizel.budget_control.utils.MissingDataException

class BudgetApiToBudgetMapper(
    private val key: String,
    private val categoryList: List<CategoryApi>
) {
    fun map(budgetApi: BudgetApi) =
        budgetApi.let {
            Budget(
                id = key,
                title = it.title ?: throw MissingDataException(),
                categoryList = mapCategoryList(categoryList),
                currency = Currency.valueOf(it.currency.orEmpty()),
                sum = it.sum?.toDoubleOrNull() ?: throw MissingDataException()
            )
    }

    private fun mapCategoryList(categoryList: List<CategoryApi>) =
        categoryList.map {
            Category(
                id = it.id ?: throw MissingDataException(),
                money = it.money?.toDoubleOrNull() ?: throw MissingDataException(),
                name = it.name ?: "Empty name",
                currency = Currency.valueOf(it.currency.orEmpty()),
                color = it.color ?: "#FFFFFF"
            )
    }
}