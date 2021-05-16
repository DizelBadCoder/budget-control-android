package dizel.budget_control.budget.mappers

import dizel.budget_control.budget.domain.Category

object CategoryToHashMapMapper {
    fun map(category: Category) = category.let {
        mapOf(
            it.id to mapOf(
                "categoryColor" to it.color,
                "categoryCurrency" to it.currency.name,
                "categoryId" to it.id,
                "categoryMoney" to it.money,
                "categoryName" to it.name
            )
        )
    }
}