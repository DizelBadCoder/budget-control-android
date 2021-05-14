package dizel.budget_control.budget.repository

import dizel.budget_control.budget.domain.Budget
import dizel.budget_control.utils.ResultRequest

interface BudgetRepository {

    suspend fun getAllBudgets(): ResultRequest<List<Budget>>

    suspend fun getBudgetById(id: String): ResultRequest<Budget>

    suspend fun postNewBudget(budget: Budget): ResultRequest<String>
}