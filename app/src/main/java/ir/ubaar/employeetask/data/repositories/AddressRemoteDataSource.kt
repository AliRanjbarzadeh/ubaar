package ir.ubaar.employeetask.data.repositories

import ir.ubaar.employeetask.core.exceptions.NetworkExceptionHandler
import ir.ubaar.employeetask.data.api.ApiService
import ir.ubaar.employeetask.data.models.AddressModel
import ir.ubaar.employeetask.data.models.AddressRequestModel
import ir.ubaar.employeetask.domain.network.NetworkResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddressRemoteDataSource @Inject constructor(
	private val apiService: ApiService,
	private val exceptionHandler: NetworkExceptionHandler,
) : AddressDataSource {
	override suspend fun list(): NetworkResult<List<AddressModel>> {
		return try {
			val result = apiService.getAddresses().await()
			return NetworkResult.Success(result)
		} catch (e: Exception) {
			NetworkResult.Error(exceptionHandler.traceErrorException(e))
		}
	}

	override suspend fun save(address: AddressRequestModel): NetworkResult<AddressModel> {
		return try {
			val result = apiService.saveAddress(address).await()
			return NetworkResult.Success(result)
		} catch (e: Exception) {
			NetworkResult.Error(exceptionHandler.traceErrorException(e))
		}
	}

}