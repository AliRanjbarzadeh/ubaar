package ir.ubaar.employeetask.core.di.modules

import android.content.Context
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ir.ubaar.employeetask.core.di.BaseHttpClient
import ir.ubaar.employeetask.core.di.BaseNetwork
import ir.ubaar.employeetask.core.di.BaseRetrofit
import ir.ubaar.employeetask.core.dispatchers.DispatchersProvider
import ir.ubaar.employeetask.core.exceptions.NetworkExceptionHandler
import ir.ubaar.employeetask.core.utilities.NetworkWatcher
import ir.ubaar.employeetask.data.api.ApiService
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
	@Provides
	@Singleton
	fun providesNetworkWatcher(@ApplicationContext context: Context, dispatchersProvider: DispatchersProvider): NetworkWatcher = NetworkWatcher(context, dispatchersProvider)

	@Provides
	@Singleton
	fun providesOkHttpClient(baseHttpClient: BaseHttpClient): OkHttpClient = baseHttpClient.okHttpClient

	@Provides
	@Singleton
	fun providesApiExceptionHandler(gson: Gson): NetworkExceptionHandler = NetworkExceptionHandler(gson)

	@Provides
	@Singleton
	fun providesCache(@ApplicationContext appContext: Context): Cache = Cache(appContext.cacheDir, 30 * 1024 * 1024)

	@Provides
	@Singleton
	fun providesBaseUrl(@ApplicationContext appContext: Context): BaseNetwork = BaseNetwork(appContext)

	@Provides
	@Singleton
	fun providesRetrofit(baseRetrofit: BaseRetrofit): Retrofit = baseRetrofit.retrofit

	@Provides
	@Singleton
	fun providesApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)
}