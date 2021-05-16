package dizel.budget_control.budget.view.create_category

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dizel.budget_control.budget.domain.Currency
import dizel.budget_control.budget.repository.use_cases.CreateCategoryUseCase
import dizel.budget_control.utils.ResultRequest
import kotlinx.coroutines.launch

class CreateCategoryViewModel(
    private val createCategoryUseCase: CreateCategoryUseCase
): ViewModel() {

    fun createCategory(params: Params): MutableLiveData<ResultRequest<Unit>> {
        val data = MutableLiveData<ResultRequest<Unit>>()
        data.value = ResultRequest.Loading

        viewModelScope.launch {
            data.value = createCategoryUseCase.execute(
                CreateCategoryUseCase.Params(
                    title = params.title,
                    color = params.color,
                    currency = params.currency,
                    money = params.money,
                    budgetId = params.budgetId
                )
            )
        }
        return data
    }


    data class Params(
        val title: String,
        val color: Int,
        val currency: Currency,
        val money: Long,
        val budgetId: String
    )
}