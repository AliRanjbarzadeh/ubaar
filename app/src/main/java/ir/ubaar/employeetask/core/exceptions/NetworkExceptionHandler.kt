package ir.ubaar.employeetask.core.exceptions

import com.google.gson.Gson
import ir.ubaar.employeetask.data.models.ErrorBody
import ir.ubaar.employeetask.data.models.ErrorMessage
import ir.ubaar.employeetask.domain.network.HttpErrors
import okhttp3.ResponseBody
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class NetworkExceptionHandler @Inject constructor(private val gson: Gson) {
	fun traceErrorException(throwable: Throwable?): ErrorMessage {
		val errorMessage: ErrorMessage = when (throwable) {

			// if throwable is an instance of HttpException
			// then attempt to parse error data from response body
			is HttpException -> {
				when(throwable.code()) {
					401 -> getHttpError(throwable.response()?.errorBody())

					403 -> ErrorMessage(
						status = HttpErrors.Forbidden,
						message = "You do not have permission to perform this operation"
					)

					500 -> ErrorMessage(
						status = HttpErrors.ServerError,
						message = "Sorry, the server has a technical problem, please try again in a few moments"
					)
				}
				// handle UNAUTHORIZED situation (when token expired)
				if (throwable.code() == 401) {
					getHttpError(throwable.response()?.errorBody())
				} else if (throwable.code() == 403) {
					ErrorMessage(
						status = HttpErrors.Forbidden,
						message = "You do not have permission to perform this operation"
					)
				} else if (throwable.code() == 500) {
					ErrorMessage(
						status = HttpErrors.ServerError,
						message = "Sorry, the server has a technical problem, please try again in a few moments"
					)
				} else {
					getHttpError(throwable.response()?.errorBody())
				}
			}

			// handle api call timeout error
			is SocketTimeoutException -> {
				ErrorMessage(
					status = HttpErrors.TimeOut,
					message = "Request Timeout"
				)
			}

			// handle connection error
			is IOException -> {
				ErrorMessage(
					status = HttpErrors.NotDefined,
					message = "Something went wrong, Please try again later"
				)
			}

			else -> ErrorMessage(
				status = HttpErrors.NotDefined,
				message = "Something went wrong, Please try again later"
			)
		}
		return errorMessage
	}

	/**
	 * attempts to parse http response body and get error data from it
	 *
	 * @param body retrofit response body
	 * @return returns an instance of [ErrorModel] with parsed data or NOT_DEFINED status
	 */
	private fun getHttpError(body: ResponseBody?): ErrorMessage {
		return try {
			val error = gson.fromJson(body?.string(), ErrorBody::class.java)
			error.toDomain()
		} catch (e: Throwable) {
			e.printStackTrace()
			ErrorMessage(status = HttpErrors.BadResponse, message = e.message.toString())
		}

	}
}