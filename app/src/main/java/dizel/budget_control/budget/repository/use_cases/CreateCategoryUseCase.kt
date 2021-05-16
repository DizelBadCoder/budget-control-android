package dizel.budget_control.budget.repository.use_cases

import dizel.budget_control.budget.domain.Currency
import dizel.budget_control.utils.ResultRequest

class CreateCategoryUseCase(
    private val setAvailableMoneyUseCase: SetAvailableMoneyUseCase
) : UseCase<CreateCategoryUseCase.Params, Unit> {

    override suspend fun execute(params: Params): ResultRequest<Unit> {
        return try {


            ResultRequest.Success(Unit)
        } catch (ex: Exception) {
            ResultRequest.Error(ex)
        }
    }

    data class Params(
        val title: String,
        val color: Int,
        val currency: Currency,
        val money: Long
    )
}