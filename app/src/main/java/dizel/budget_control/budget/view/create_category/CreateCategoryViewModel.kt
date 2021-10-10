package dizel.budget_control.budget.view.create_category

import androidx.lifecycle.MutableLiveData
import dizel.budget_control.budget.domain.entity.Currency
import dizel.budget_control.budget.domain.use_cases.CreateCategoryUseCase
import dizel.budget_control.core.utils.BaseViewModel
import dizel.budget_control.core.utils.ResultRequest

class CreateCategoryViewModel(
    private val createCategoryUseCase: CreateCategoryUseCase
): BaseViewModel() {

    fun createCategory(params: Params): MutableLiveData<ResultRequest<Unit>> {
        val data = MutableLiveData<ResultRequest<Unit>>()
        data.value = ResultRequest.Loading

        val paramsUseCase = CreateCategoryUseCase.Params(
            title = params.title,
            color = params.color,
            currency = params.currency,
            money = params.money,
            budgetId = params.budgetId
        )
        makeRequest(
            { createCategoryUseCase.execute(paramsUseCase) },
            { data.value = ResultRequest.Success(Unit) }
        )
        return data
    }


    data class Params(
        val title: String,
        val color: Int,
        val currency: Currency,
        val money: Double,
        val budgetId: String
    )
}