package ir.ubaar.employeetask.domain.repositories

import ir.ubaar.employeetask.data.models.AddressModel
import ir.ubaar.employeetask.domain.network.NetworkResult

interface AddressRepository {
	suspend fun list(): NetworkResult<List<AddressModel>>
	suspend fun save(rawBody: String): NetworkResult<AddressModel>
}