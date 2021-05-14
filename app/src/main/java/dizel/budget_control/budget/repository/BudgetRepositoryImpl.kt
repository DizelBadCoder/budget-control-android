package dizel.budget_control.budget.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dizel.budget_control.budget.domain.Budget
import dizel.budget_control.budget.domain.BudgetApi
import dizel.budget_control.budget.domain.CategoryApi
import dizel.budget_control.budget.mappers.BudgetApiToBudgetMapper
import dizel.budget_control.budget.mappers.BudgetToHashMapMapper
import dizel.budget_control.utils.ResultRequest
import kotlinx.coroutines.tasks.await
import timber.log.Timber

class BudgetRepositoryImpl(
    private val database: FirebaseDatabase,
    private val auth: FirebaseAuth
): BudgetRepository {

    override suspend fun getAllBudgets(): ResultRequest<List<Budget>> {
        return try {

            val snapshots = getBudgets()

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
            val snapshots = getBudgets()

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

    override suspend fun postNewBudget(budget: Budget): ResultRequest<String> {
        return try {
            val hashMap = BudgetToHashMapMapper.map(budget)
            val key = hashMap.keys.first()

            val budgetsLength = getReference()
                .child("BudgetsLength")
                .get()
                .await()
                .getValue(Long::class.java) ?: 0

            getReference()
                .child("BudgetsLength")
                .setValue(budgetsLength + 1)

            getReference()
                .child("Budgets")
                .updateChildren(hashMap)

            ResultRequest.Success(key)
        } catch (ex: Exception) {
            ResultRequest.Error(ex)
        }
    }

    private suspend fun getBudgets() = getReference()
        .child("Budgets")
        .get()
        .await()
        .children

    private fun getReference(): DatabaseReference {
        val user = auth.currentUser ?: throw Exception("User is invalid!")
        return database.reference
            .child("users")
            .child(user.uid)
    }
}

