package dizel.budget_control.budget.di

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dizel.budget_control.budget.view.budget_details.BudgetDetailsViewModel
import dizel.budget_control.budget.view.budget_list.BudgetListViewModel
import dizel.budget_control.budget.view.create_budget.CreateBudgetViewModel
import dizel.budget_control.budget.view.create_category.CreateCategoryViewModel
import dizel.budget_control.budget.domain.repository.BudgetRepository
import dizel.budget_control.budget.domain.repository.BudgetRepositoryImpl
import dizel.budget_control.budget.domain.use_cases.*
import dizel.budget_control.budget.domain.repository.DatabaseRepository
import dizel.budget_control.budget.domain.repository.DatabaseRepositoryImpl
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val budgetsModule = module {
    single { Firebase.database }
    single { Firebase.auth }

    single<BudgetRepository> { BudgetRepositoryImpl(get()) }
    single<DatabaseRepository> { DatabaseRepositoryImpl(get(), get()) }

    viewModel { BudgetListViewModel(get()) }
    viewModel { BudgetDetailsViewModel(get(), get(), get(), get()) }
    viewModel { CreateBudgetViewModel(get()) }
    viewModel { CreateCategoryViewModel(get()) }

    factory { CreateBudgetUseCase(get()) }
    factory { RemoveBudgetUseCase(get()) }
    factory { CreateCategoryUseCase(get(), get()) }
    factory { RemoveCategoryUseCase(get(), get()) }
    factory { RenameBudgetUseCase(get()) }
    factory { SetAvailableMoneyUseCase(get()) }
}
