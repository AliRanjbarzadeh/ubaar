package ir.ubaar.employeetask.data.api

import ir.ubaar.employeetask.data.models.AddressModel
import kotlinx.coroutines.Deferred
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
	//Get addresses list
	@GET("address")
	fun getAddresses(): Deferred<List<AddressModel>>

	//Save address
	@POST
	fun saveAddress(@Body address: String): Deferred<AddressModel>
}