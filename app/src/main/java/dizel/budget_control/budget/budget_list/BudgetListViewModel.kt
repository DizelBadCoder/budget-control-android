package dizel.budget_control.budget.budget_list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dizel.budget_control.budget.domain.Budget
import dizel.budget_control.budget.repository.BudgetRepository
import dizel.budget_control.utils.ResultRequest
import dizel.budget_control.utils.asLiveData
import kotlinx.coroutines.launch

class BudgetListViewModel(
    private val budgetRepository: BudgetRepository
): ViewModel() {
    private val _budgetList = MutableLiveData<ResultRequest<List<Budget>>>()
    val budgetList = _budgetList.asLiveData()

    init {
        loadBudgetList()
    }

    private fun loadBudgetList() {
        viewModelScope.launch {
            _budgetList.value = ResultRequest.Loading
            _budgetList.value = budgetRepository.getAllBudgets()
        }
    }
}