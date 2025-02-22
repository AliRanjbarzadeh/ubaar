package ir.ubaar.employeetask.core

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.gms.location.Priority
import com.google.android.gms.location.SettingsClient

class LocationServiceProvider(
	private val context: Context,
	private val locationSettingsRequestLauncher: ActivityResultLauncher<IntentSenderRequest>,
) {
	private var locationManager: LocationManager? = null
	private var locationListener: LocationListener? = null
	private var locationUpdateCallback: LocationUpdateCallback? = null

	// Interface for callback to pass location data to the fragment
	interface LocationUpdateCallback {
		fun onLocationUpdated(location: Location) {}
	}

	// Register the callback when fragment is attached
	fun setLocationUpdateCallback(callback: LocationUpdateCallback) {
		this.locationUpdateCallback = callback
	}

	// Start location updates
	@SuppressLint("MissingPermission")
	fun startLocationUpdates() {
		locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

		// Check if location services are enabled
		if (!isLocationEnabled()) {
			promptToTurnOnLocation()
			return
		}

		locationListener = LocationListener { location -> locationUpdateCallback?.onLocationUpdated(location) }

		// Request location updates (best available provider, min time = 60000ms, min distance = 0m)
		locationManager?.requestLocationUpdates(
			LocationManager.GPS_PROVIDER,
			60000L,
			0f,
			locationListener!!
		)
	}

	// Stop location updates
	fun stopLocationUpdates() {
		locationManager?.removeUpdates(locationListener!!)
	}

	// Fetch current location
	@SuppressLint("MissingPermission")
	private fun getCurrentLocation() {
		locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)?.let { location ->
			locationUpdateCallback?.onLocationUpdated(location)
		}
	}

	// Check if location services (GPS and network) are enabled
	private fun isLocationEnabled(): Boolean {
		val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
		return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
				locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
	}

	// Prompt the user to turn on location settings if they are off using LocationSettingsRequest
	private fun promptToTurnOnLocation() {
		val mHighAccuracy = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
			.build()

		val mBalancedAccuracy = LocationRequest.Builder(Priority.PRIORITY_BALANCED_POWER_ACCURACY, 5000)
			.build()

		val builder = LocationSettingsRequest
			.Builder()
			.addLocationRequest(mHighAccuracy)
			.addLocationRequest(mBalancedAccuracy)
			.setNeedBle(true)

		val client: SettingsClient = LocationServices.getSettingsClient(context)
		val task = client.checkLocationSettings(builder.build())

		task.addOnSuccessListener {
			// Location is enabled
			startLocationUpdates()
		}

		task.addOnFailureListener { exception ->
			if (exception is ApiException) {
				if (exception.statusCode == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
					try {
						val resolvable = exception as ResolvableApiException
						val intentSenderRequest = IntentSenderRequest.Builder(resolvable.resolution).build()
						locationSettingsRequestLauncher.launch(intentSenderRequest)
					} catch (ex: Exception) {
						ex.printStackTrace()
					}
				}
			}
		}
	}
}
