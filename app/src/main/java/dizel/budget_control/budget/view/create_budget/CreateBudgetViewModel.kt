package dizel.budget_control.budget.view.create_budget

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dizel.budget_control.budget.domain.Budget
import dizel.budget_control.budget.repository.BudgetRepository
import dizel.budget_control.utils.ResultRequest
import dizel.budget_control.utils.asLiveData
import kotlinx.coroutines.launch

class CreateBudgetViewModel(
    private val budgetRepository: BudgetRepository
): ViewModel() {

    private val _createBudgetFlow = MutableLiveData<ResultRequest<String>>()
    val createBudgetFlow = _createBudgetFlow.asLiveData()

    fun createBudget(budget: Budget) {
        viewModelScope.launch {
            _createBudgetFlow.value = ResultRequest.Loading
            _createBudgetFlow.value = budgetRepository.createBudget(budget)
        }
    }
}