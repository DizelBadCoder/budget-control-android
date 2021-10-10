package dizel.budget_control.budget.view.budget_list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dizel.budget_control.budget.domain.entity.Budget
import dizel.budget_control.budget.domain.repository.BudgetRepository
import dizel.budget_control.core.utils.ResultRequest
import dizel.budget_control.core.extensions.asLiveData
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class BudgetListViewModel(
    private val budgetRepository: BudgetRepository
): ViewModel() {
    private val _budgetList = MutableLiveData<ResultRequest<List<Budget>>>()
    val budgetList = _budgetList.asLiveData()

    private val _budgetDetailFlow = MutableSharedFlow<String>()
    val budgetDetailFlow = _budgetDetailFlow.asSharedFlow()

    init {
        loadBudgetList()
    }

    fun loadBudgetList() {
        viewModelScope.launch {
            _budgetList.value = ResultRequest.Loading
            _budgetList.value = budgetRepository.getAllBudgets()
        }
    }

    fun navigateToBudgetDetail(budgetId: String) {
        viewModelScope.launch {
            _budgetDetailFlow.emit(budgetId)
        }
    }
}