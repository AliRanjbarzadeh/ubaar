package ir.ubaar.employeetask.core.helpers

import ir.ubaar.employeetask.core.Configs
import ir.ubaar.employeetask.core.extensions.loadFromSp


object LanguageHelper {
	fun getLanguage(): String {
		return loadFromSp(Configs.Sessions.LANGUAGE, Configs.Defaults.LANGUAGE)
	}

	fun isDefault(language: String): Boolean {
		return language == Configs.Defaults.LANGUAGE
	}
}