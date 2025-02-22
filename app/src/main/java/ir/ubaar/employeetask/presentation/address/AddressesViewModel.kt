package ir.ubaar.employeetask.presentation.address

import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.ubaar.employeetask.core.BaseViewModel
import ir.ubaar.employeetask.core.dispatchers.DispatchersProvider
import ir.ubaar.employeetask.data.models.AddressModel
import ir.ubaar.employeetask.domain.network.NetworkResult
import ir.ubaar.employeetask.domain.usecases.AddressListUseCase
import javax.inject.Inject

@HiltViewModel
class AddressesViewModel @Inject constructor(
	dispatchersProvider: DispatchersProvider,
	private val addressListUseCase: AddressListUseCase,
) : BaseViewModel(dispatchersProvider) {
	private var currentIndex = 0
	private val pageSize = 30
	private var allAddresses = emptyList<AddressModel>()
	private val _addresses: MutableLiveData<NetworkResult<List<AddressModel>>> = MutableLiveData()
	var isFetched = false

	init {
		fetchData()
	}

	fun fetchData(isRefresh: Boolean = false) {
		if (!isRefresh && isFetched)
			return

		execute {
			_isLoading.postValue(true)
			val result = addressListUseCase()
			if (result is NetworkResult.Success) {
				allAddresses = result.data
				loadNextChunk()
			} else if (result is NetworkResult.Error) {
				_addresses.postValue(result)
			}
			_isLoading.postValue(false)
		}
	}

	fun loadNextChunk() {
		if (currentIndex >= allAddresses.size)
			return
		val nextBatch = allAddresses.drop(currentIndex).take(pageSize)
		currentIndex += nextBatch.size
		_addresses.postValue(NetworkResult.Success(nextBatch))
	}

	fun getAddresses(): MutableLiveData<NetworkResult<List<AddressModel>>> = _addresses
}