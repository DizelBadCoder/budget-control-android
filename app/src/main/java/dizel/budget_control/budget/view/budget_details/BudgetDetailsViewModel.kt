package dizel.budget_control.budget.view.budget_details

import androidx.lifecycle.MutableLiveData
import dizel.budget_control.budget.domain.entity.Budget
import dizel.budget_control.budget.domain.repository.BudgetRepository
import dizel.budget_control.budget.domain.use_cases.RemoveBudgetUseCase
import dizel.budget_control.budget.domain.use_cases.RemoveCategoryUseCase
import dizel.budget_control.budget.domain.use_cases.RenameBudgetUseCase
import dizel.budget_control.core.utils.BaseViewModel
import dizel.budget_control.core.utils.ResultRequest
import dizel.budget_control.core.extensions.asLiveData

class BudgetDetailsViewModel(
    private val budgetRepository: BudgetRepository,
    private val removeCategoryUseCase: RemoveCategoryUseCase,
    private val removeBudgetUseCase: RemoveBudgetUseCase,
    private val renameBudgetUseCase: RenameBudgetUseCase,
): BaseViewModel() {
    private var budgetId: String? = null

    private val _budget = MutableLiveData<Budget>()
    val budget = _budget.asLiveData()

    fun loadBudgetById(budgetId: String) {
        this.budgetId = budgetId

        makeRequest(
            { budgetRepository.getBudgetById(budgetId) },
            { _budget.value = it }
        )
    }

    fun deleteBudget(): MutableLiveData<ResultRequest<Unit>> {
        val data = MutableLiveData<ResultRequest<Unit>>()
        data.value = ResultRequest.Loading

        makeRequest(
            { removeBudgetUseCase.execute(RemoveBudgetUseCase.Params(budgetId!!)) },
            { data.value = ResultRequest.Success(Unit) }
        )

        return data
    }

    fun renameBudget(name: String) {
        makeRequest(
            { renameBudgetUseCase.execute(RenameBudgetUseCase.Params(budgetId!!, name)) },
            { retry() }
        )
    }

    fun removeCategory(categoryId: String) {
        makeRequest(
            { removeCategoryUseCase.execute(RemoveCategoryUseCase.Params(budgetId!!, categoryId)) },
            { retry() }
        )
    }

    fun retry() {
        loadBudgetById(budgetId!!)
    }
}