package dizel.budget_control.budget.domain.repository

import dizel.budget_control.budget.domain.entity.Budget
import dizel.budget_control.budget.domain.entity.BudgetApi
import dizel.budget_control.budget.domain.entity.CategoryApi
import dizel.budget_control.budget.domain.mappers.BudgetApiToBudgetMapper
import dizel.budget_control.core.utils.ResultRequest

class BudgetRepositoryImpl(
    private val databaseRepository: DatabaseRepository
): BudgetRepository {

    override suspend fun getAllBudgets(): ResultRequest<List<Budget>> {
        return try {
            val snapshots = databaseRepository.getBudgets()

            val budgets = snapshots.map { snapshot ->

                val categoryList = snapshot
                    .child("category")
                    .children
                    .map {
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
            val snapshots = databaseRepository.getBudgets()

            val snapshot = snapshots.find { it.key.orEmpty() == id }

            val budgetApi = snapshot?.getValue(BudgetApi::class.java)!!
            val categoryApiList = snapshot
                .child("category")
                .children
                .map {
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

