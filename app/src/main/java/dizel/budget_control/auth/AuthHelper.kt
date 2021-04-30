package dizel.budget_control.auth

import  android.util.Patterns.EMAIL_ADDRESS

fun isValidPassword(password: String) = password.length > 5

fun isValidEmail(email: String) =
        email.isNotEmpty() && EMAIL_ADDRESS.matcher(email).matches()