package ir.ubaar.employeetask.core.di.modules

import android.content.ContentResolver
import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ir.ubaar.employeetask.core.di.BaseHttpClient
import ir.ubaar.employeetask.core.di.BaseNetwork
import ir.ubaar.employeetask.core.di.BaseRetrofit
import ir.ubaar.employeetask.core.dispatchers.DispatchersProvider
import ir.ubaar.employeetask.core.dispatchers.DispatchersProviderImpl
import ir.ubaar.employeetask.core.exceptions.NetworkExceptionHandler
import ir.ubaar.employeetask.core.utilities.Logger
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.Executors
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

	@Provides
	@Singleton
	fun providesContentResolver(@ApplicationContext appContext: Context): ContentResolver = appContext.contentResolver

	@Provides
	@Singleton
	fun providesGson(): Gson = GsonBuilder().create()

	@Provides
	@Singleton
	fun providesDispatcher(dispatcherProvider: DispatchersProviderImpl): DispatchersProvider = dispatcherProvider.dispatcher

	@Provides
	@Singleton
	fun providesLogger(@ApplicationContext appContext: Context): Logger = Logger(appContext)
}