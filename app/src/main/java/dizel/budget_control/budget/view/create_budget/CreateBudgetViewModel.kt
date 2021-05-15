package dizel.budget_control.budget.view.create_budget

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dizel.budget_control.budget.domain.Budget
import dizel.budget_control.budget.domain.Category
import dizel.budget_control.budget.domain.Currency
import dizel.budget_control.budget.repository.BudgetRepository
import dizel.budget_control.budget.repository.use_cases.CreateBudgetUseCase
import dizel.budget_control.utils.ResultRequest
import dizel.budget_control.utils.asLiveData
import dizel.budget_control.utils.generateKey
import kotlinx.coroutines.launch

class CreateBudgetViewModel(
    private val createBudgetUseCase: CreateBudgetUseCase
): ViewModel() {

    private val _createBudgetFlow = MutableLiveData<ResultRequest<String>>()
    val createBudgetFlow = _createBudgetFlow.asLiveData()

    fun createBudget(params: NewBudgetParams) {
        val useCaseParams = CreateBudgetUseCase.Params(
            title = params.title,
            money = params.money,
            currency = params.currency
        )

        viewModelScope.launch {
            _createBudgetFlow.value = ResultRequest.Loading
            _createBudgetFlow.value = createBudgetUseCase.execute(useCaseParams)
        }
    }

    data class NewBudgetParams(
        val title: String,
        val money: Long,
        val currency: Currency
    )
}