package dizel.budget_control.budget.use_cases

import dizel.budget_control.budget.domain.BudgetApi
import dizel.budget_control.budget.domain.Category
import dizel.budget_control.budget.domain.CategoryApi
import dizel.budget_control.utils.DatabaseHelper
import dizel.budget_control.utils.ResultRequest
import kotlinx.coroutines.tasks.await

class SetAvailableMoneyUseCase(
    private val databaseHelper: DatabaseHelper
) : UseCase<SetAvailableMoneyUseCase.Params, Unit> {

    override suspend fun execute(params: Params): ResultRequest<Unit> {
        return try {
            val budgetId = params.budgetId

            val snapshot = databaseHelper.getBudgets()
                .find { it.key.orEmpty() == budgetId }

            val budgetApi = snapshot?.getValue(BudgetApi::class.java)!!
            val categoryApiList = snapshot.child("category").children.map {
                it.getValue(CategoryApi::class.java)!!
            }

            val sumCategoryList = categoryApiList
                .filterNot { it.id.orEmpty() == Category.AVAILABLE_MONEY_KEY }
                .map { it.money?.toDoubleOrNull() ?: 0.0 }
                .sum()

            val availableMoney = (budgetApi.sum?.toDoubleOrNull() ?: 0.0) - sumCategoryList

            val reference = snapshot
                .child("category")
                .child(Category.AVAILABLE_MONEY_KEY)
                .child("categoryMoney")
                .ref

            reference.setValue(availableMoney).await()

            ResultRequest.Success(Unit)
        } catch (ex: Exception) {
            ResultRequest.Error(ex)
        }
    }

    data class Params(
        val budgetId: String
    )
}