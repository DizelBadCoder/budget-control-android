package dizel.budget_control.core.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import timber.log.Timber

open class BaseViewModel: ViewModel() {

    private val _errorFlow = MutableSharedFlow<Throwable>()
    val errorFlow = _errorFlow.asSharedFlow()

    protected fun emitError(ex: Throwable) {
        Timber.e(ex)
        viewModelScope.launch { _errorFlow.emit(ex) }
    }
    /**
     * Utility for work with coroutines
     */
    protected fun <T> makeRequest(
        request: suspend () -> ResultRequest<T>,
        onSuccess: (T) -> Unit = { },
        onFailure: (Throwable) -> Unit = { emitError(it) }
    ) {
        viewModelScope.launch {
            when (val result = request()) {
                is ResultRequest.Success -> onSuccess(result.data)
                is ResultRequest.Error -> onFailure(result.exception)
            }
        }
    }

}