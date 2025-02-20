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
class AddressViewModel @Inject constructor(
	dispatchersProvider: DispatchersProvider,
	private val addressListUseCase: AddressListUseCase,
) : BaseViewModel(dispatchersProvider) {
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
			_addresses.postValue(result)
			_isLoading.postValue(false)
		}
	}

	fun getAddresses(): MutableLiveData<NetworkResult<List<AddressModel>>> = _addresses
}