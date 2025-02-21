package ir.ubaar.employeetask.core.di

import android.util.Base64
import ir.ubaar.employeetask.core.Configs
import okhttp3.Interceptor
import okhttp3.Response
import java.nio.charset.StandardCharsets
import javax.inject.Inject


class ModifyHeadersInterceptor @Inject constructor() : Interceptor {
	override fun intercept(chain: Interceptor.Chain): Response {
		val request = chain.request()
		val builder = request.newBuilder()

		try {
			builder.header("Cache-Control", "no-cache")
			builder.header("Content-Type", "application/json")

			val credentials = "${Configs.Authenticate.USERNAME}:${Configs.Authenticate.PASSWORD}"
			val authorization = "Basic " + Base64.encodeToString(credentials.toByteArray(StandardCharsets.UTF_8), Base64.NO_WRAP)
			builder.header("Authorization", authorization)
		} catch (ex: Exception) {
//			e(ex.message, "HeaderErrors")
		}

		val builderResponse = chain.proceed(builder.build())
		return builderResponse
	}
}