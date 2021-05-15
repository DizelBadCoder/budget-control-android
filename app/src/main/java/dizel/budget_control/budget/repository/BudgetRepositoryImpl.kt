package dizel.budget_control.budget.repository

import dizel.budget_control.budget.domain.Budget
import dizel.budget_control.budget.domain.BudgetApi
import dizel.budget_control.budget.domain.CategoryApi
import dizel.budget_control.budget.mappers.BudgetApiToBudgetMapper
import dizel.budget_control.utils.DatabaseHelper
import dizel.budget_control.utils.ResultRequest
import timber.log.Timber

class BudgetRepositoryImpl(
    private val databaseHelper: DatabaseHelper
): BudgetRepository {

    override suspend fun getAllBudgets(): ResultRequest<List<Budget>> {
        return try {

            val snapshots = databaseHelper.getBudgets()

            val budgets = snapshots.map { snapshot ->

                val categoryList = snapshot.child("category").children.map {
                    Timber.d(it.value.toString())
                    it.getValue(CategoryApi::class.java)!!
                }

                val key = snapshot.key!!
                BudgetApiToBudgetMapper(key, categoryList).map(
                    snapshot.getValue(BudgetApi::class.java)!!
                )
            }

            ResultRequest.Success(budgets)
        } catch (ex: Exception) {
            ResultRequest.Error(ex)
        }
    }

    override suspend fun getBudgetById(id: String): ResultRequest<Budget> {
        return try {
            val snapshots = databaseHelper.getBudgets()

            val snapshot = snapshots.find { it.key.orEmpty() == id }

            val budgetApi = snapshot?.getValue(BudgetApi::class.java)!!
            val categoryApiList = snapshot.child("category").children.map {
                it.getValue(CategoryApi::class.java)!!
            }

            val key = snapshot.key!!
            val budget = BudgetApiToBudgetMapper(key, categoryApiList).map(budgetApi)

            ResultRequest.Success(budget)
        } catch (ex: Exception) {
            ResultRequest.Error(ex)
        }
    }
}

