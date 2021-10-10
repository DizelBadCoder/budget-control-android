package dizel.budget_control.budget.domain.use_cases

import dizel.budget_control.budget.domain.entity.Budget
import dizel.budget_control.budget.domain.entity.Category
import dizel.budget_control.budget.domain.entity.Currency
import dizel.budget_control.budget.domain.mappers.BudgetToHashMapMapper
import dizel.budget_control.budget.domain.repository.DatabaseRepository
import dizel.budget_control.core.utils.ResultRequest
import dizel.budget_control.core.tools.generateKey
import kotlinx.coroutines.tasks.await

class CreateBudgetUseCase(
    private val databaseRepository: DatabaseRepository
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

            val budgetsLength = (databaseRepository.getReference()
                .child("BudgetsLength")
                .get()
                .await()
                .getValue(Long::class.java) ?: 0) + 1

            databaseRepository.getReference()
                .child("BudgetsLength")
                .setValue(budgetsLength)

            databaseRepository.getReference()
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