package ir.ubaar.employeetask.data.repositories

import ir.ubaar.employeetask.data.models.AddressModel
import ir.ubaar.employeetask.domain.network.NetworkResult

interface AddressDataSource {
	suspend fun list(): NetworkResult<List<AddressModel>>
	suspend fun save(rawBody: String): NetworkResult<AddressModel>
}