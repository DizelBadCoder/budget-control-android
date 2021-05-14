package dizel.budget_control.budget.view.create_budget

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dizel.budget_control.budget.domain.Budget
import dizel.budget_control.budget.domain.Category
import dizel.budget_control.budget.domain.Currency
import dizel.budget_control.budget.repository.BudgetRepository
import dizel.budget_control.utils.ResultRequest
import dizel.budget_control.utils.asLiveData
import dizel.budget_control.utils.generateKey
import kotlinx.coroutines.launch

class CreateBudgetViewModel(
    private val budgetRepository: BudgetRepository
): ViewModel() {

    private val _createBudgetFlow = MutableLiveData<ResultRequest<String>>()
    val createBudgetFlow = _createBudgetFlow.asLiveData()

    private fun postNewBudget(budget: Budget) {
        viewModelScope.launch {
            _createBudgetFlow.value = ResultRequest.Loading
            _createBudgetFlow.value = budgetRepository.postNewBudget(budget)
        }
    }

    fun createBudget(params: NewBudgetParams) {
        val categoryList = listOf(
            Category(
                id = Category.AVAILABLE_MONEY_KEY,
                name = Category.AVAILABLE_MONEY_KEY,
                color = Category.DEFAULT_COLOR,
                money = params.money,
                currency = params.currency
            )
        )

        val budget = Budget(
            id = generateKey(),
            title = params.title,
            sum = params.money,
            currency = params.currency,
            categoryList = categoryList
        )

        postNewBudget(budget)
    }

    data class NewBudgetParams(
        val title: String,
        val money: Long,
        val currency: Currency
    )
}