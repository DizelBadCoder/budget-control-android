package dizel.budget_control.budget.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dizel.budget_control.budget.domain.Budget
import dizel.budget_control.budget.domain.BudgetApi
import dizel.budget_control.budget.domain.BudgetApiToBudgetMapper
import dizel.budget_control.budget.domain.BudgetToHashMap
import dizel.budget_control.utils.ResultRequest

class BudgetRepositoryImpl(
    private val database: FirebaseDatabase,
    private val auth: FirebaseAuth
): BudgetRepository {

    override suspend fun getAllBudgets(): ResultRequest<List<Budget>> {
        return try {
            val snapshots = getReference()
                .get().result?.children

            val budgets = snapshots?.map {
                BudgetApiToBudgetMapper.map(
                    it.getValue(BudgetApi::class.java)!!
                )
            }.orEmpty()

            ResultRequest.Success(budgets)
        } catch (ex: Exception) {
            ResultRequest.Error(ex)
        }
    }

    override suspend fun getBudgetById(id: String): ResultRequest<Budget> {
        return try {
            val snapshots = getReference()
                .get().result?.children

            val budgetApi = snapshots
                ?.find { it.key.orEmpty() == id }
                ?.getValue(BudgetApi::class.java)!!

            val budget = BudgetApiToBudgetMapper.map(budgetApi)

            ResultRequest.Success(budget)
        } catch (ex: Exception) {
            ResultRequest.Error(ex)
        }
    }

    override suspend fun createBudget(budget: Budget): ResultRequest<Unit> {
        return try {
            val hashMap = BudgetToHashMap.map(budget)
            getReference().updateChildren(hashMap)

            ResultRequest.Success(Unit)
        } catch (ex: Exception) {
            ResultRequest.Error(ex)
        }
    }

    private fun getReference(): DatabaseReference {
        val user = auth.currentUser ?: throw Exception("User is invalid!")

        return database
            .getReference("users/${user.uid}/Budgets")
    }

}

