package dizel.budget_control.budget.domain.use_cases

import dizel.budget_control.budget.domain.repository.DatabaseRepository
import dizel.budget_control.core.utils.ResultRequest
import kotlinx.coroutines.tasks.await
import java.lang.IllegalArgumentException

class RemoveBudgetUseCase(
    private val databaseRepository: DatabaseRepository
) : UseCase<RemoveBudgetUseCase.Params, Unit>{

    override suspend fun execute(params: Params): ResultRequest<Unit> {
        return try {
            val snapshot = databaseRepository.findBudgetById(params.budgetId)
                ?: throw IllegalArgumentException("Budget exceeds available funds")

            snapshot.ref.removeValue().await()

            val budgetsLength = (databaseRepository.getReference()
                .child("BudgetsLength")
                .get()
                .await()
                .getValue(Long::class.java) ?: 0) - 1

            databaseRepository.getReference()
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