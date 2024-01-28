package base

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * A base view model designed to wrap [Composable] functions with associated business logic.
 *
 * This class provides core utilities and structures to manage coroutine lifecycles within the
 * scope of a ViewModel. It ensures that the coroutines are automatically cancelled and cleaned up
 * when the ViewModel is no longer in use.
 *
 * @author https://blog.stackademic.com/viewmodel-management-with-jetpack-compose-6951906486e6
 */
abstract class ComposeViewModel {

    /**
     * Represents a [SupervisorJob] that oversees all coroutine jobs initiated by this ViewModel.
     * Utilizing a supervisor allows child jobs to fail independently without affecting others.
     */
    protected val viewModelSupervisor = SupervisorJob()

    /**
     * A coroutine scope specifically for this ViewModel, determined by [viewModelSupervisor].
     * All coroutines launched within this ViewModel should use this scope to ensure they are
     * tied to the ViewModel's lifecycle.
     */
    protected val viewModelScope = CoroutineScope(viewModelSupervisor)

    private val _uiEvent = Channel<UiEvent?>()
    val uiEvent: Flow<UiEvent?> = _uiEvent.receiveAsFlow()

    /**
     * Abstract function that should be overridden by subclasses to clear or release resources
     * and jobs when the ViewModel is about to be disposed of.
     *
     * This function gets called when [clearViewModel] is executed, before the supervisor gets cancelled.
     */
    open suspend fun onClear() {}

    /**
     * Handles the process of cleaning up and releasing all resources tied to the ViewModel.
     * It first triggers the [onClear] method and, upon completion, cancels the [viewModelSupervisor]
     * which in turn cancels all active jobs under its jurisdiction.
     */
    fun clearViewModel() {
        viewModelScope.launch {
            onClear()
        }.invokeOnCompletion {
            viewModelSupervisor.cancel("Compose View Model Job Cancelled")
        }
    }

    fun resetEvent() {
        viewModelScope.launch {
            _uiEvent.send(null)
        }
    }

    fun sendEvent(uiEvent: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(uiEvent)
        }
    }

    fun dismissCurrentSnackbar() {
        viewModelScope.launch {
            sendEvent(UiEvent.DismissCurrentSnackbar)
        }
    }
}
