package dizel.budget_control.budget.repository.use_cases

import dizel.budget_control.budget.domain.Category
import dizel.budget_control.budget.domain.Currency
import dizel.budget_control.budget.mappers.CategoryToHashMapMapper
import dizel.budget_control.utils.DatabaseHelper
import dizel.budget_control.utils.MissingDataException
import dizel.budget_control.utils.ResultRequest
import dizel.budget_control.utils.generateKey

class CreateCategoryUseCase(
    private val databaseHelper: DatabaseHelper,
    private val setAvailableMoneyUseCase: SetAvailableMoneyUseCase
) : UseCase<CreateCategoryUseCase.Params, Unit> {

    override suspend fun execute(params: Params): ResultRequest<Unit> {
        return try {

            val budget = databaseHelper.getBudgets()
                .find { it.key.orEmpty() == params.budgetId }

            val category = Category(
                id = generateKey(),
                name = params.title,
                money = params.money,
                currency = params.currency,
                color = String.format("#%06X", (0xFFFFFF and params.color))
            )

            val categoryMap = CategoryToHashMapMapper.map(category)

            budget
                ?.child("category")
                ?.ref
                ?.updateChildren(categoryMap)

            setAvailableMoneyUseCase.execute(
                SetAvailableMoneyUseCase.Params(params.budgetId)
            )

            ResultRequest.Success(Unit)
        } catch (ex: Exception) {
            ResultRequest.Error(ex)
        }
    }

    data class Params(
        val title: String,
        val color: Int,
        val currency: Currency,
        val money: Long,
        val budgetId: String
    )
}