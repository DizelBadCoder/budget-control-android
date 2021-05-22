package dizel.budget_control.budget.view.create_budget

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dizel.budget_control.budget.domain.Currency
import dizel.budget_control.budget.use_cases.CreateBudgetUseCase
import dizel.budget_control.utils.ResultRequest
import kotlinx.coroutines.launch

class CreateBudgetViewModel(
    private val createBudgetUseCase: CreateBudgetUseCase
): ViewModel() {

    fun createBudget(
        params: NewBudgetParams
    ): MutableLiveData<ResultRequest<String>> {
        val data = MutableLiveData<ResultRequest<String>>()

        data.value = ResultRequest.Loading

        viewModelScope.launch {
            data.value = createBudgetUseCase.execute(
                CreateBudgetUseCase.Params(
                    title = params.title,
                    money = params.money,
                    currency = params.currency
                )
            )
        }
        return data
    }

    data class NewBudgetParams(
        val title: String,
        val money: Double,
        val currency: Currency
    )
}