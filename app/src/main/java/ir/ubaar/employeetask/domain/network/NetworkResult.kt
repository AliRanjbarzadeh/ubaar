package ir.ubaar.employeetask.domain.network

import ir.ubaar.employeetask.data.models.ErrorBody

sealed class NetworkResult<T> {
	data class Success<T>(val data: T) : NetworkResult<T>()
	data class Error<T>(val data: ErrorBody) : NetworkResult<T>()
}