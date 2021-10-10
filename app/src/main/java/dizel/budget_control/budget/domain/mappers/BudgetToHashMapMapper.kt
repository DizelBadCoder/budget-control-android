package dizel.budget_control.budget.domain.mappers

import dizel.budget_control.budget.domain.entity.Budget
import dizel.budget_control.budget.domain.entity.Category
import dizel.budget_control.core.tools.generateKey

object BudgetToHashMapMapper {
    fun map(budget: Budget): HashMap<String, Any> {
        val key = budget.id
        val map = budget.toHashMap()

        return hashMapOf(key to map)
    }

    fun mapCategoryListToHashMap(categoryList: List<Category>): HashMap<String, Any> {
        val hashMap = HashMap<String, Any>()

        categoryList
            .map {  category -> category.toHashMap() }
            .forEach {
                val isAvailableMoneyCategory = it["categoryId"] as String == Category.AVAILABLE_MONEY_KEY

                val key = when (isAvailableMoneyCategory) {
                    true -> Category.AVAILABLE_MONEY_KEY
                    false -> generateKey()
                }

                hashMap[key] = it
            }

        return hashMap
    }
}