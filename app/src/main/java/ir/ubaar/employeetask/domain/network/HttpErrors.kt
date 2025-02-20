package ir.ubaar.employeetask.domain.network


enum class HttpErrors {
	Unauthorized,
	ServerError,
	TimeOut,
	BadRequest,
	Forbidden,
	BadResponse,
	NotDefined,
	Conflict
}