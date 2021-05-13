package dizel.budget_control.budget.mappers

import dizel.budget_control.budget.domain.Budget
import dizel.budget_control.budget.domain.Category
import dizel.budget_control.utils.generateKey

object BudgetToHashMapMapper {
    fun map(budget: Budget): HashMap<String, Any> {
        val key = budget.id
        val map = hashMapOf<String, Any>(
            "title" to budget.title,
            "budgetSum" to budget.sum,
            "currency" to budget.currency.name,
            "category" to mapCategory(budget.categoryList)
        )

        return hashMapOf(key to map)
    }

    private fun mapCategory(categoryList: List<Category>): HashMap<String, Any> {
        val hashMap = HashMap<String, Any>()

        categoryList
            .map {
                hashMapOf<String, Any>(
                    "categoryId" to it.id,
                    "categoryName" to it.name,
                    "categoryMoney" to it.money,
                    "categoryCurrency" to it.currency.name,
                    "categoryColor" to it.color
                )
            }
            .forEach {
                if (it["categoryId"] as String == Category.AVAILABLE_MONEY_KEY) {
                    hashMap[Category.AVAILABLE_MONEY_KEY] = it
                } else {
                    hashMap[generateKey()] = it
                }
            }

        return hashMap
    }
}