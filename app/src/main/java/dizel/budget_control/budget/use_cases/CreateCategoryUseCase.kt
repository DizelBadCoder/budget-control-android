package dizel.budget_control.budget.use_cases

import dizel.budget_control.budget.domain.Category
import dizel.budget_control.budget.domain.Currency
import dizel.budget_control.budget.mappers.CategoryToHashMapMapper
import dizel.budget_control.utils.ConvertMoney
import dizel.budget_control.utils.DatabaseHelper
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

            val budgetCurrency = budget
                ?.child("currency")
                ?.getValue(String::class.java)!!

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
                .child("category/${Category.AVAILABLE_MONEY_KEY}/categoryMoney")
                .getValue(Double::class.java) ?: 0.0

            if (availableMoney < category.money) {
                val ex = IllegalArgumentException("Category budget exceeds available funds")
                return ResultRequest.Error(ex)
            }

            budget.child("category").ref
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