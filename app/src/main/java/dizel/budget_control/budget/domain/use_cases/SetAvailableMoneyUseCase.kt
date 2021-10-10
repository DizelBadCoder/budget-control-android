package dizel.budget_control.budget.domain.use_cases

import dizel.budget_control.budget.domain.entity.BudgetApi
import dizel.budget_control.budget.domain.entity.Category
import dizel.budget_control.budget.domain.entity.CategoryApi
import dizel.budget_control.budget.domain.repository.DatabaseRepository
import dizel.budget_control.core.utils.ResultRequest
import kotlinx.coroutines.tasks.await
import java.lang.IllegalArgumentException

class SetAvailableMoneyUseCase(
    private val databaseRepository: DatabaseRepository
) : UseCase<SetAvailableMoneyUseCase.Params, Unit> {

    override suspend fun execute(params: Params): ResultRequest<Unit> {
        return try {
            val snapshot = databaseRepository.findBudgetById(params.budgetId)
                ?: throw IllegalArgumentException("Budget exceeds available funds")

            val budgetApi = snapshot.getValue(BudgetApi::class.java)!!
            val categoryApiList = snapshot.child("category").children.map {
                it.getValue(CategoryApi::class.java)!!
            }

            val sumCategoryList = categoryApiList
                .filterNot { it.id.orEmpty() == Category.AVAILABLE_MONEY_KEY }
                .map { it.money?.toDoubleOrNull() ?: 0.0 }
                .sum()

            val availableMoney = (budgetApi.sum?.toDoubleOrNull() ?: 0.0) - sumCategoryList

            val reference = snapshot
                .child(Category.PATH_TO_AVAILABLE_MONEY_CATEGORY)
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