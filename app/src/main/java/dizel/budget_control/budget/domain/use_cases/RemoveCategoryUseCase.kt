package dizel.budget_control.budget.domain.use_cases

import dizel.budget_control.budget.domain.repository.DatabaseRepository
import dizel.budget_control.core.utils.ResultRequest
import kotlinx.coroutines.tasks.await
import java.lang.IllegalArgumentException

class RemoveCategoryUseCase(
    private val databaseRepository: DatabaseRepository,
    private val setAvailableMoneyUseCase: SetAvailableMoneyUseCase
) : UseCase<RemoveCategoryUseCase.Params, Unit> {

    override suspend fun execute(params: Params): ResultRequest<Unit> {
        return try {

            val snapshot = databaseRepository.findBudgetById(params.budgetId)
                ?: throw IllegalArgumentException("Budget exceeds available funds")

            val category = snapshot
                .child("category")
                .children
                .find { it.key.orEmpty() == params.categoryId }

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