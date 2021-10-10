package dizel.budget_control.budget.domain.repository

import dizel.budget_control.budget.domain.entity.Budget
import dizel.budget_control.core.utils.ResultRequest

interface BudgetRepository {

    suspend fun getAllBudgets(): ResultRequest<List<Budget>>

    suspend fun getBudgetById(id: String): ResultRequest<Budget>

}