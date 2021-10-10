package dizel.budget_control.budget.domain.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class DatabaseRepositoryImpl(
    private val database: FirebaseDatabase,
    private val auth: FirebaseAuth
): DatabaseRepository {

    override suspend fun findBudgetById(id: String): DataSnapshot? {
        return getBudgets()
            .find { it.key.orEmpty() == id }
    }

    override suspend fun getBudgets(): MutableIterable<DataSnapshot> {
        return getReference()
            .child("Budgets")
            .get()
            .await()
            .children
    }

    override fun getReference(): DatabaseReference {
        val user = auth.currentUser ?: throw Exception("User is invalid!")
        return database.reference
            .child("users")
            .child(user.uid)
    }
}