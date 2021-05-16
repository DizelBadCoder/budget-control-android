package dizel.budget_control.budget.repository.use_cases

import dizel.budget_control.utils.DatabaseHelper
import dizel.budget_control.utils.ResultRequest
import kotlinx.coroutines.tasks.await

class RemoveBudgetUseCase(
    private val databaseHelper: DatabaseHelper
) : UseCase<RemoveBudgetUseCase.Params, Unit>{

    override suspend fun execute(params: Params): ResultRequest<Unit> {
        return try {
            val budgetId = params.budgetId

            val snapshot = databaseHelper.getBudgets()
                .find { it.key.orEmpty() == budgetId }

            snapshot?.ref?.removeValue()?.await()

            val budgetsLength = (databaseHelper.getReference()
                .child("BudgetsLength")
                .get()
                .await()
                .getValue(Long::class.java) ?: 0) - 1

            databaseHelper.getReference()
                .child("BudgetsLength")
                .setValue(budgetsLength)

            ResultRequest.Success(Unit)
        } catch (ex: Exception) {
            ResultRequest.Error(ex)
        }
    }

    data class Params(
        val budgetId: String
    )
}