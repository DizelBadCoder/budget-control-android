package dizel.budget_control.budget.domain.use_cases

import dizel.budget_control.core.utils.ResultRequest

interface UseCase<T, R> {
    suspend fun execute(params: T): ResultRequest<R>
}