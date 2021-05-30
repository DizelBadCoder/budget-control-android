package dizel.budget_control.budget.view.budget_details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dizel.budget_control.budget.domain.Budget
import dizel.budget_control.budget.repository.BudgetRepository
import dizel.budget_control.budget.use_cases.RemoveBudgetUseCase
import dizel.budget_control.budget.use_cases.RemoveCategoryUseCase
import dizel.budget_control.budget.use_cases.RenameBudgetUseCase
import dizel.budget_control.utils.MissingDataException
import dizel.budget_control.utils.ResultRequest
import dizel.budget_control.utils.asLiveData
import kotlinx.coroutines.launch

class BudgetDetailsViewModel(
    private val budgetRepository: BudgetRepository,
    private val removeCategoryUseCase: RemoveCategoryUseCase,
    private val removeBudgetUseCase: RemoveBudgetUseCase,
    private val renameBudgetUseCase: RenameBudgetUseCase,
): ViewModel() {
    private var budgetId: String? = null

    private val _budget = MutableLiveData<ResultRequest<Budget>>()
    val budget = _budget.asLiveData()

    fun loadBudgetById(budgetId: String) {
        this.budgetId = budgetId

        viewModelScope.launch {
            _budget.value = ResultRequest.Loading
            _budget.value = budgetRepository.getBudgetById(budgetId)
        }
    }

    fun deleteBudget(): MutableLiveData<ResultRequest<Unit>> {
        val data = MutableLiveData<ResultRequest<Unit>>()
        data.value = ResultRequest.Loading

        viewModelScope.launch {
            data.value = removeBudgetUseCase.execute(
                RemoveBudgetUseCase.Params(budgetId!!)
            )
        }

        return data
    }

    fun renameBudget(name: String): MutableLiveData<ResultRequest<Unit>> {
        val data = MutableLiveData<ResultRequest<Unit>>()
        data.value = ResultRequest.Loading

        viewModelScope.launch {
            data.value = renameBudgetUseCase.execute(
                RenameBudgetUseCase.Params(budgetId!!, name)
            )
        }

        return data
    }


    fun retry() {
        budgetId?.let { loadBudgetById(it) }
    }
}