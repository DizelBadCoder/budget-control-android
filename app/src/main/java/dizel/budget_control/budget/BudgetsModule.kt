package dizel.budget_control.budget

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dizel.budget_control.budget.budget_details.BudgetDetailsViewModel
import dizel.budget_control.budget.budget_list.BudgetListViewModel
import dizel.budget_control.budget.create_budget.CreateBudgetViewModel
import dizel.budget_control.budget.create_category.CreateCategoryViewModel
import dizel.budget_control.budget.repository.BudgetRepository
import dizel.budget_control.budget.repository.BudgetRepositoryImpl
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val budgetsModule = module {
    single<BudgetRepository> { BudgetRepositoryImpl(get(), get()) }

    single { Firebase.database }
    single { Firebase.auth }

    viewModel { BudgetListViewModel(get()) }
    viewModel { BudgetDetailsViewModel(get()) }
    viewModel { CreateBudgetViewModel(get()) }
    viewModel { CreateCategoryViewModel(get()) }
}
