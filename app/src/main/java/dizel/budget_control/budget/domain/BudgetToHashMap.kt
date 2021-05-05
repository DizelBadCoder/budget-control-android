package dizel.budget_control.budget.domain

import dizel.budget_control.utils.generateKey

object BudgetToHashMap {
    fun map(budget: Budget): HashMap<String, Any> {
        val key = generateKey()
        val map = hashMapOf<String, Any>(
            "title" to budget.title,
            "sum" to budget.sum,
            "currency" to budget.currency.name,
            "category" to mapCategory(budget.category)
        )

        return hashMapOf(key to map)
    }

    private fun mapCategory(categoryList: List<Category>): HashMap<String, Any> {
        val hashMap = HashMap<String, Any>()
        categoryList
            .map { hashMapOf<String, Any>(
                "id" to it.id,
                "name" to it.name,
                "money" to it.money,
                "currency" to it.currency.name,
                "color" to it.color
            ) }
            .forEach {
                hashMap[generateKey()] = it
            }
        return hashMap
    }
}