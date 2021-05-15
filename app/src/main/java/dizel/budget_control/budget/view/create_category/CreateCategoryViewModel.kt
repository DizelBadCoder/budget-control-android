package dizel.budget_control.budget.view.create_category

import androidx.lifecycle.ViewModel
import dizel.budget_control.budget.repository.BudgetRepository
import dizel.budget_control.budget.repository.use_cases.CreateCategoryUseCase

class CreateCategoryViewModel(
    private val createCategoryUseCase: CreateCategoryUseCase
): ViewModel()