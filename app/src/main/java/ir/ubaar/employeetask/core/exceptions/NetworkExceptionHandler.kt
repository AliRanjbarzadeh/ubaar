package ir.ubaar.employeetask.core.exceptions

import com.google.gson.Gson
import ir.ubaar.employeetask.core.utilities.Logger
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
class NetworkExceptionHandler @Inject constructor(private val gson: Gson, private val logger: Logger) {
	fun traceErrorException(throwable: Throwable?): ErrorBody {
		logger.debug(throwable?.message, "HTTP_EXP")
		val errorBody: ErrorBody = when (throwable) {

			// if throwable is an instance of HttpException
			// then attempt to parse error data from response body
			is HttpException -> {
				logger.debug(throwable.code(), "HTTP_EXP")
				getHttpError(throwable.code(), throwable.response()?.errorBody())
			}

			// handle api call timeout error
			is SocketTimeoutException -> {
				ErrorBody(
					status = false,
					message = "زمان پاسخ سرور پایان یافته، لطفا دوباره تلاش کنید"
				)
			}

			// handle connection error
			is IOException -> {
				ErrorBody(
					status = false,
					message = "خطایی در اتصال به سرور رخ داده است، لطفا دوباره تلاش کنید",
				)
			}

			else -> ErrorBody(
				status = false,
				message = "خطایی نامشخص، لطفا دوباره تلاش کنید"
			)
		}
		return errorBody
	}

	/**
	 * attempts to parse http response body and get error data from it
	 *
	 * @param body retrofit response body
	 * @return returns an instance of [ErrorBody] with parsed data or NOT_DEFINED status
	 */
	private fun getHttpError(statusCode: Int, body: ResponseBody?): ErrorBody {
		return try {
			val error = gson.fromJson(body?.string(), ErrorBody::class.java)
			error.statusCode = HttpErrors.fromValue(statusCode)
			error
		} catch (e: Throwable) {
			e.printStackTrace()
			ErrorBody(status = false, message = e.message.toString())
		}

	}
}