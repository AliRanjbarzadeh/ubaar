package ir.ubaar.employeetask.core

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ir.ubaar.employeetask.core.dispatchers.DispatchersProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel(private val dispatchersProvider: DispatchersProvider) : ViewModel(), CoroutineScope {
	protected val TAG = this::class.java.simpleName + "Log"
	open protected val _isLoading: MutableLiveData<Boolean> = MutableLiveData()

	override val coroutineContext: CoroutineContext
		get() = dispatchersProvider.getMain() + SupervisorJob()

	fun execute(job: suspend () -> Unit) = launch {
		withContext(dispatchersProvider.getIO()) { job.invoke() }
	}

	open fun isLoading(): MutableLiveData<Boolean> = _isLoading
}