package ir.ubaar.employeetask.core.di.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ir.ubaar.employeetask.data.repositories.AddressRemoteDataSource
import ir.ubaar.employeetask.data.repositories.AddressRepositoryImpl
import ir.ubaar.employeetask.domain.repositories.AddressRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
	@Provides
	@Singleton
	fun providesAddressRepository(addressDataSource: AddressRemoteDataSource): AddressRepository = AddressRepositoryImpl(addressDataSource)
}