package dizel.budget_control.budget.domain.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference

interface DatabaseRepository {

    suspend fun findBudgetById(id: String): DataSnapshot?
    suspend fun getBudgets(): MutableIterable<DataSnapshot>
    fun getReference(): DatabaseReference

}