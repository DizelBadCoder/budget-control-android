package dizel.budget_control.firebase

import dizel.budget_control.budget_list.Budget
import dizel.budget_control.budget_list.Category
import dizel.budget_control.budget_list.Currency

class DatabaseHelper {

    fun getAllBudgets(): List<Budget> {
        // TODO remove stub

        val list = ArrayList<Budget>()

        (0..10).map { index ->
            val category = ArrayList<Category>()
            (0..10).map {
                category.add(Category(
                        id = "id_${it}",
                        name = "Category #${it}",
                        money = it * 34.1,
                        currency = Currency.RUB,
                        color = "#123321В"
                ))
            }

            list.add(Budget(
                    sum = category.map { it.money }.sum(),
                    category = category,
                    currency = Currency.RUB,
                    title = "Budget #${index}"
            ))
        }

        return list
    }
}