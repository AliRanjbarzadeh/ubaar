package ir.ubaar.employeetask.data.models

import ir.ubaar.employeetask.domain.network.HttpErrors


data class ErrorMessage(
	val message: String?,
	val status: HttpErrors,
)