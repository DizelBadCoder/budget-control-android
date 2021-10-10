package dizel.budget_control.core.utils

sealed class ResultRequest<out T> {
    data class Success<out T>(val data: T) : ResultRequest<T>()
    data class Error(val exception: Throwable) : ResultRequest<Nothing>()
    object Loading : ResultRequest<Nothing>()
}