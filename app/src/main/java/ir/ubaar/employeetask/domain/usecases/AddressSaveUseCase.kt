package ir.ubaar.employeetask.domain.usecases

import ir.ubaar.employeetask.data.models.AddressModel
import ir.ubaar.employeetask.data.models.AddressRequestModel
import ir.ubaar.employeetask.domain.network.NetworkResult
import ir.ubaar.employeetask.domain.repositories.AddressRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddressSaveUseCase @Inject constructor(private val repository: AddressRepository) {
	suspend operator fun invoke(address: AddressRequestModel): NetworkResult<AddressModel> = repository.save(address)
}