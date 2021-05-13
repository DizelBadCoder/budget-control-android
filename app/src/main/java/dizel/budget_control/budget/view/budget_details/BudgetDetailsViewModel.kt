package dizel.budget_control.budget.view.budget_details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dizel.budget_control.budget.domain.Budget
import dizel.budget_control.budget.repository.BudgetRepository
import dizel.budget_control.utils.ResultRequest
import dizel.budget_control.utils.asLiveData
import kotlinx.coroutines.launch

class BudgetDetailsViewModel(
    private val budgetRepository: BudgetRepository
): ViewModel() {

    private val _budget = MutableLiveData<ResultRequest<Budget>>()
    val budget = _budget.asLiveData()

    fun loadBudgetById(budgetId: String) {
        viewModelScope.launch {
            _budget.value = ResultRequest.Loading
            _budget.value = budgetRepository.getBudgetById(budgetId)
        }
    }
}