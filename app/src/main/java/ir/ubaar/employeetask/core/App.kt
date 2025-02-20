package ir.ubaar.employeetask.core

import androidx.multidex.MultiDexApplication
import com.orhanobut.hawk.Hawk
import dagger.hilt.android.HiltAndroidApp
import ir.ubaar.employeetask.core.helpers.FontHelper
import ir.ubaar.employeetask.core.helpers.LanguageHelper
import ir.ubaar.employeetask.core.helpers.LocaleHelper

@HiltAndroidApp
class App : MultiDexApplication() {
	override fun onCreate() {
		super.onCreate()
		initHawk()
		initLanguage()
		initFont()
	}

	private fun initHawk() {
		Hawk.init(this).build()
	}

	private fun initLanguage() {
		LocaleHelper.setLocale(this, LanguageHelper.getLanguage())
	}

	private fun initFont() {
		FontHelper.setFont(this)
	}
}