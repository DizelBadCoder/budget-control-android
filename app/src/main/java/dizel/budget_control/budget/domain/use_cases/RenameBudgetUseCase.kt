package dizel.budget_control.budget.domain.use_cases

import dizel.budget_control.budget.domain.repository.DatabaseRepository
import dizel.budget_control.core.utils.ResultRequest
import kotlinx.coroutines.tasks.await
import java.lang.IllegalArgumentException

class RenameBudgetUseCase(
    private val databaseRepository: DatabaseRepository
): UseCase<RenameBudgetUseCase.Params, Unit> {

    override suspend fun execute(params: Params): ResultRequest<Unit> {
        return try {
            val budget = databaseRepository.findBudgetById(params.budgetId)
                ?: throw IllegalArgumentException("Budget exceeds available funds")

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