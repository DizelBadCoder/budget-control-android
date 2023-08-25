package dizel.budget_control.auth.view

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import dizel.budget_control.R

Button(onClick = { navController.navigate("signIn") }) {
    Text(text = "Sign In")
}

Button(onClick = { navController.navigate("signUp") }) {
    Text(text = "Sign Up")
}