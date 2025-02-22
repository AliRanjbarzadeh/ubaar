package ir.ubaar.employeetask.data.repositories

import ir.ubaar.employeetask.data.models.AddressModel
import ir.ubaar.employeetask.data.models.AddressRequestModel
import ir.ubaar.employeetask.domain.network.NetworkResult
import ir.ubaar.employeetask.domain.repositories.AddressRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddressRepositoryImpl @Inject constructor(private val dataSource: AddressDataSource) : AddressRepository {
	override suspend fun list(): NetworkResult<List<AddressModel>> = dataSource.list()
	override suspend fun save(address: AddressRequestModel): NetworkResult<AddressModel> = dataSource.save(address)
}