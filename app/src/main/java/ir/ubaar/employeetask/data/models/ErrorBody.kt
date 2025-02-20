package ir.ubaar.employeetask.data.models

import com.google.gson.JsonElement
import ir.ubaar.employeetask.domain.network.HttpErrors


data class ErrorBody(
	val message: String? = "",
	val data: JsonElement? = null,
	val status: Boolean? = false,
	var statusCode: HttpErrors = HttpErrors.NotDefined,
) {
	override fun toString(): String {
		return "ErrorBody(message=$message, data=$data, status=$status, statusCode=$statusCode)"
	}
}