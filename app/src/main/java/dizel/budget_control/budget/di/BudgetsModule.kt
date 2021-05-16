package dizel.budget_control.budget.di

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dizel.budget_control.budget.view.budget_details.BudgetDetailsViewModel
import dizel.budget_control.budget.view.budget_list.BudgetListViewModel
import dizel.budget_control.budget.view.create_budget.CreateBudgetViewModel
import dizel.budget_control.budget.view.create_category.CreateCategoryViewModel
import dizel.budget_control.budget.repository.BudgetRepository
import dizel.budget_control.budget.repository.BudgetRepositoryImpl
import dizel.budget_control.budget.repository.use_cases.*
import dizel.budget_control.utils.DatabaseHelper
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val budgetsModule = module {
    single { Firebase.database }
    single { Firebase.auth }

    single { DatabaseHelper(get(), get()) }

    single<BudgetRepository> { BudgetRepositoryImpl(get()) }

    viewModel { BudgetListViewModel(get()) }
    viewModel { BudgetDetailsViewModel(get(), get(), get()) }
    viewModel { CreateBudgetViewModel(get()) }
    viewModel { CreateCategoryViewModel(get()) }

    factory { CreateBudgetUseCase(get()) }
    factory { RemoveBudgetUseCase(get()) }
    factory { CreateCategoryUseCase(get()) }
    factory { RemoveCategoryUseCase() }
    factory { SetAvailableMoneyUseCase(get()) }
}
