package dizel.budget_control.budget.domain.use_cases

import dizel.budget_control.budget.domain.entity.Category
import dizel.budget_control.budget.domain.entity.Currency
import dizel.budget_control.budget.domain.mappers.CategoryToHashMapMapper
import dizel.budget_control.core.tools.ConvertMoney
import dizel.budget_control.budget.domain.repository.DatabaseRepository
import dizel.budget_control.core.utils.ResultRequest
import dizel.budget_control.core.tools.generateKey

class CreateCategoryUseCase(
    private val databaseRepository: DatabaseRepository,
    private val setAvailableMoneyUseCase: SetAvailableMoneyUseCase
) : UseCase<CreateCategoryUseCase.Params, Unit> {

    override suspend fun execute(params: Params): ResultRequest<Unit> {
        return try {
            val budget = databaseRepository.findBudgetById(params.budgetId)
                ?: throw java.lang.IllegalArgumentException("Budget exceeds available funds")

            val budgetCurrency = budget
                .child("currency")
                .getValue(String::class.java)!!

            val money = ConvertMoney.convert(
                money = params.money,
                from = params.currency,
                to = Currency.valueOf(budgetCurrency)
            )

            val category = Category(
                id = generateKey(),
                name = params.title,
                money = money,
                currency = Currency.valueOf(budgetCurrency),
                color = String.format("#%06X", (0xFFFFFF and params.color)) // convert int to hex
            )

            val categoryMap = CategoryToHashMapMapper.map(category)

            val availableMoney = budget
                .child(Category.PATH_TO_AVAILABLE_MONEY_CATEGORY)
                .getValue(Double::class.java) ?: 0.0

            if (availableMoney < category.money) {
                val ex = IllegalArgumentException("Category budget exceeds available funds")
                return ResultRequest.Error(ex)
            }

            budget.child("category")
                .ref
                .updateChildren(categoryMap)

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
        val money: Double,
        val budgetId: String
    )
}