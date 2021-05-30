package dizel.budget_control.budget.use_cases

import dizel.budget_control.utils.DatabaseHelper
import dizel.budget_control.utils.ResultRequest
import kotlinx.coroutines.tasks.await

class RenameBudgetUseCase(
    private val databaseHelper: DatabaseHelper
): UseCase<RenameBudgetUseCase.Params, Unit> {

    override suspend fun execute(params: Params): ResultRequest<Unit> {
        return try {
            val budget = databaseHelper.getBudgets()
                .find { params.budgetId == it.key }!!

            budget.child("title")
                .ref.setValue(params.name).await()

            ResultRequest.Success(Unit)
        } catch (ex: Exception) {
            ResultRequest.Error(ex)
        }
    }

    data class Params(
        val budgetId: String,
        val name: String
    )
}