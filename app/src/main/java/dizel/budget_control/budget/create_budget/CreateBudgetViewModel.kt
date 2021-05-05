package dizel.budget_control.budget.create_budget

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

    private val _loadingFlow = MutableLiveData<ResultRequest<Unit>>()
    val loadingFlow = _loadingFlow.asLiveData()

    fun createBudget(budget: Budget) {
        viewModelScope.launch {
            _loadingFlow.value = ResultRequest.Loading
            _loadingFlow.value = budgetRepository.createBudget(budget)
        }
    }
}