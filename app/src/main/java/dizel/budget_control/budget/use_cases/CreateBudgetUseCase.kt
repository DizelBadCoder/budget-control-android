package dizel.budget_control.budget.use_cases

import dizel.budget_control.budget.domain.Budget
import dizel.budget_control.budget.domain.Category
import dizel.budget_control.budget.domain.Currency
import dizel.budget_control.budget.mappers.BudgetToHashMapMapper
import dizel.budget_control.utils.DatabaseHelper
import dizel.budget_control.utils.ResultRequest
import dizel.budget_control.utils.generateKey
import kotlinx.coroutines.tasks.await

class CreateBudgetUseCase(
    private val databaseHelper: DatabaseHelper
): UseCase<CreateBudgetUseCase.Params, String>{
    override suspend fun execute(params: Params): ResultRequest<String> {
        return try {
            val categoryList = listOf(
                Category(
                    id = Category.AVAILABLE_MONEY_KEY,
                    name = Category.AVAILABLE_MONEY_KEY,
                    color = Category.DEFAULT_COLOR,
                    money = params.money,
                    currency = params.currency
                )
            )

            val budget = Budget(
                id = generateKey(),
                title = params.title,
                sum = params.money,
                currency = params.currency,
                categoryList = categoryList
            )

            val hashMap = BudgetToHashMapMapper.map(budget)
            val key = hashMap.keys.first()

            val budgetsLength = (databaseHelper.getReference()
                .child("BudgetsLength")
                .get()
                .await()
                .getValue(Long::class.java) ?: 0) + 1

            databaseHelper.getReference()
                .child("BudgetsLength")
                .setValue(budgetsLength)

            databaseHelper.getReference()
                .child("Budgets")
                .updateChildren(hashMap)

            ResultRequest.Success(key)
        } catch (ex: Exception) {
            ResultRequest.Error(ex)
        }
    }

    data class Params(
        val title: String,
        val money: Double,
        val currency: Currency
    )
}