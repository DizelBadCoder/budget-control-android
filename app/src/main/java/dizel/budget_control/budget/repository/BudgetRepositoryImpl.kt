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
            val snapshots = getReference()
                .get().await().children

            val budgets = snapshots.map { snapshot ->

                val categoryList = snapshot.child("category").children.map {
                    Timber.d(it.value.toString())
                    it.getValue(CategoryApi::class.java)!!
                }

                Timber.d(snapshot.getValue(BudgetApi::class.java)!!.toString())
                BudgetApiToBudgetMapper(categoryList).map(
                    snapshot.getValue(BudgetApi::class.java)!!
                )
            }

            Timber.d(budgets.toString())
            ResultRequest.Success(budgets)
        } catch (ex: Exception) {
            ResultRequest.Error(ex)
        }
    }

    override suspend fun getBudgetById(id: String): ResultRequest<Budget> {
        return try {
            val snapshots = getReference()
                    .get().await().children

            val snapshot = snapshots.find { it.key.orEmpty() == id }

            val budgetApi = snapshot?.getValue(BudgetApi::class.java)!!
            val categoryApiList = snapshot.child("category").children.map {
                it.getValue(CategoryApi::class.java)!!
            }

            val budget = BudgetApiToBudgetMapper(categoryApiList).map(budgetApi)

            ResultRequest.Success(budget)
        } catch (ex: Exception) {
            ResultRequest.Error(ex)
        }
    }

    override suspend fun createBudget(budget: Budget): ResultRequest<Unit> {
        return try {
            val hashMap = BudgetToHashMapMapper.map(budget)
            getReference().updateChildren(hashMap)

            ResultRequest.Success(Unit)
        } catch (ex: Exception) {
            ResultRequest.Error(ex)
        }
    }

    private fun getReference(): DatabaseReference {
        val user = auth.currentUser ?: throw Exception("User is invalid!")
        return database.getReference("users/${user.uid}/Budgets")
    }

}

