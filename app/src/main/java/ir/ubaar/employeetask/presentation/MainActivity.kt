package ir.ubaar.employeetask.presentation

import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import ir.ubaar.employeetask.R
import ir.ubaar.employeetask.core.BaseActivity
import ir.ubaar.employeetask.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

	private lateinit var navHostFragment: NavHostFragment

	override fun onCreate(savedInstanceState: Bundle?) {
		installSplashScreen()
		super.onCreate(savedInstanceState)

		navHostFragment = supportFragmentManager.findFragmentById(R.id.base_nav_host) as NavHostFragment
	}

	override fun onSupportNavigateUp(): Boolean {
		return navHostFragment.navController.navigateUp()
	}
}