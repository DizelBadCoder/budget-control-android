package dizel.budget_control.budget.use_cases

import dizel.budget_control.utils.ResultRequest

interface UseCase<T, R> {
    suspend fun execute(params: T): ResultRequest<R>
}