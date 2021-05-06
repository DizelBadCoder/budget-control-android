package dizel.budget_control.budget.domain

import com.google.firebase.database.PropertyName

data class BudgetApi(
    @get:PropertyName("budgetSum")
    @set:PropertyName("budgetSum")
    var sum: Long? = null,

    @get:PropertyName("currency")
    @set:PropertyName("currency")
    var currency: String? = null,

    @get:PropertyName("title")
    @set:PropertyName("title")
    var title: String? = null
)

data class CategoryApi(
    @get:PropertyName("categoryId")
    @set:PropertyName("categoryId")
    var id: String? = null,

    @get:PropertyName("categoryName")
    @set:PropertyName("categoryName")
    var name: String? = null,

    @get:PropertyName("categoryMoney")
    @set:PropertyName("categoryMoney")
    var money: Long? = null,

    @get:PropertyName("categoryCurrency")
    @set:PropertyName("categoryCurrency")
    var currency: String? = null,

    @get:PropertyName("categoryColor")
    @set:PropertyName("categoryColor")
    var color: String? = null

)