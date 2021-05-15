package dizel.budget_control.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class DatabaseHelper(
    private val database: FirebaseDatabase,
    private val auth: FirebaseAuth
) {

    suspend fun getBudgets() = getReference()
        .child("Budgets")
        .get()
        .await()
        .children

    fun getReference(): DatabaseReference {
        val user = auth.currentUser ?: throw Exception("User is invalid!")
        return database.reference
            .child("users")
            .child(user.uid)
    }

}