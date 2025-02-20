package ir.ubaar.employeetask.core.di

import android.content.Context
import ir.ubaar.employeetask.core.Configs
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BaseNetwork @Inject constructor(context: Context) {
	val baseUrl: String = Configs.Defaults.BASE_URL
}