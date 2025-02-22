package ir.ubaar.employeetask.presentation.address.add

import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.ubaar.employeetask.core.BaseViewModel
import ir.ubaar.employeetask.core.dispatchers.DispatchersProvider
import ir.ubaar.employeetask.data.models.AddressModel
import ir.ubaar.employeetask.data.models.AddressRequestModel
import ir.ubaar.employeetask.domain.network.NetworkResult
import ir.ubaar.employeetask.domain.usecases.AddressSaveUseCase
import javax.inject.Inject

@HiltViewModel
class AddressMapViewModel @Inject constructor(
	dispatchersProvider: DispatchersProvider,
	private val addressSaveUseCase: AddressSaveUseCase,
) : BaseViewModel(dispatchersProvider) {
	private val _address: MutableLiveData<NetworkResult<AddressModel>> = MutableLiveData()

	fun saveAddress(addressRequestModel: AddressRequestModel) {
		execute {
			_isLoading.postValue(true)
			val result = addressSaveUseCase(addressRequestModel)
			_address.postValue(result)
			_isLoading.postValue(false)
		}
	}

	fun addressSaved(): MutableLiveData<NetworkResult<AddressModel>> = _address
}