pluginManagement {
	repositories {
//		maven { setUrl("https://android.ranjbarzadehali.workers.dev/") }
		google {
			content {
				includeGroupByRegex("com\\.android.*")
				includeGroupByRegex("com\\.google.*")
				includeGroupByRegex("androidx.*")
			}
		}
		mavenCentral()
		gradlePluginPortal()
	}
}
dependencyResolutionManagement {
	repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
	repositories {
//		maven { setUrl("https://android.ranjbarzadehali.workers.dev/") }
		google()
		mavenCentral()
		maven { setUrl( "https://jitpack.io") }
	}
}

rootProject.name = "Employee Task"
include(":app")
