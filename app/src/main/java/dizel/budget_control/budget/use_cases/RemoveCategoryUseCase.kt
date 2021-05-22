package dizel.budget_control.budget.use_cases

import dizel.budget_control.utils.DatabaseHelper
import dizel.budget_control.utils.ResultRequest
import kotlinx.coroutines.tasks.await

class RemoveCategoryUseCase(
    private val databaseHelper: DatabaseHelper,
    private val setAvailableMoneyUseCase: SetAvailableMoneyUseCase
) : UseCase<RemoveCategoryUseCase.Params, Unit> {

    override suspend fun execute(params: Params): ResultRequest<Unit> {
        return try {

            val snapshot = databaseHelper.getBudgets()
                .find { it.key.orEmpty() == params.budgetId }

            val category = snapshot
                ?.child("category")
                ?.children
                ?.find { it.key.orEmpty() == params.categoryId }

            category?.ref?.removeValue()?.await()

            setAvailableMoneyUseCase.execute(
                SetAvailableMoneyUseCase.Params(
                    budgetId = params.budgetId
                )
            )

            ResultRequest.Success(Unit)
        } catch (ex: Exception) {
            ResultRequest.Error(ex)
        }
    }

    data class Params(
        val budgetId: String,
        val categoryId: String
    )

}