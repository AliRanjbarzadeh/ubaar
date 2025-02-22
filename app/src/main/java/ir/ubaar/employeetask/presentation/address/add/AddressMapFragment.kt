package ir.ubaar.employeetask.presentation.address.add

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.afollestad.materialdialogs.MaterialDialog
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import ir.ubaar.employeetask.R
import ir.ubaar.employeetask.core.BaseFragment
import ir.ubaar.employeetask.core.LocationServiceProvider
import ir.ubaar.employeetask.core.extensions.observe
import ir.ubaar.employeetask.data.models.AddressModel
import ir.ubaar.employeetask.data.models.AddressRequestModel
import ir.ubaar.employeetask.databinding.FragmentAddressMapBinding
import ir.ubaar.employeetask.domain.network.NetworkResult
import ir.ubaar.employeetask.presentation.FragmentResults

@AndroidEntryPoint
class AddressMapFragment : BaseFragment<FragmentAddressMapBinding>(
	resId = R.layout.fragment_address_map,
	titleResId = R.string.location_on_map,
	isShowBackButton = true
), LocationServiceProvider.LocationUpdateCallback {

	private val args: AddressMapFragmentArgs by navArgs()
	private val viewModel: AddressMapViewModel by viewModels()

	private lateinit var addressRequestModel: AddressRequestModel

	private lateinit var mapFragment: SupportMapFragment
	private lateinit var target: LatLng
	private var mGoogleMap: GoogleMap? = null
	private lateinit var locationProvider: LocationServiceProvider

	val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
		if (permissions.filter { !it.value }.isEmpty()) {
			turnOnLocation()
		}
	}

	private val locationSettingsRequestLauncher =
		registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
			// Handle the result when the user interacts with the location settings dialog
			if (result.resultCode == Activity.RESULT_OK) {
				// Location settings were enabled
				locationProvider.startLocationUpdates()
			} else {
				// User did not enable location services
				Toast.makeText(requireContext(), getString(R.string.location_service_did_not_enable), Toast.LENGTH_SHORT).show()
			}
		}

	private val mapReadyCallback = OnMapReadyCallback { googleMap: GoogleMap ->
		mGoogleMap = googleMap
		setMapCenter()
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		addressRequestModel = args.addressRequest

		target = LatLng(29.623986, 52.482004)

		setupObservers()
	}

	override fun onStart() {
		super.onStart()

		// Initialize LocationProvider and register the callback
		locationProvider = LocationServiceProvider(requireContext(), locationSettingsRequestLauncher)
		locationProvider.setLocationUpdateCallback(this)
	}

	override fun onStop() {
		super.onStop()

		locationProvider.stopLocationUpdates()
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		loadMapFragment()

		binding.fabLocation.setOnClickListener {
			grantPermissionAndTurnOnLocation()
		}

		binding.btnSave.setOnClickListener {
			//Set map center as address location
			mGoogleMap?.cameraPosition?.target?.also {
				addressRequestModel.lat = it.latitude
				addressRequestModel.lng = it.longitude
			}

			viewModel.saveAddress(addressRequestModel)
		}
	}

	override fun getMainView(): ViewGroup {
		return binding.mainView
	}

	override fun onLocationUpdated(location: Location) {
		setMapCenter(location)
	}

	private fun setupObservers() {
		viewModel.run {
			observe(isLoading(), ::initLoading)
			observe(addressSaved(), ::addressSaved)
		}
	}

	private fun addressSaved(result: NetworkResult<AddressModel>) {
		if (result is NetworkResult.Success) {
			logger.debug(result.data)

			setFragmentResult(FragmentResults.Address.saved, bundleOf(FragmentResults.saved to result.data))
			findNavController().popBackStack(R.id.addressesFragment, false)
		} else if (result is NetworkResult.Error) {
			val errorMessage = if (result.data.message?.isNotEmpty() == true) {
				result.data.message
			} else {
				getString(R.string.response_error)
			}

			MaterialDialog(requireContext())
				.show {
					//Set dialog message
					message(text = errorMessage)

					//Try again
					positiveButton(R.string.try_again) {
						binding.btnSave.callOnClick()
						dismiss()
					}

					//Close the dialog
					negativeButton(R.string.close) {
						dismiss()
					}
				}
		}
	}

	private fun grantPermissionAndTurnOnLocation() {
		val allPermissions = mutableListOf(
			Manifest.permission.ACCESS_FINE_LOCATION,
			Manifest.permission.ACCESS_COARSE_LOCATION,
		)

		val necessaryPermissions = allPermissions.filter {
			ContextCompat.checkSelfPermission(requireContext(), it) != PackageManager.PERMISSION_GRANTED
		}

		if (necessaryPermissions.any()) {
			if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
				MaterialDialog(requireContext())
					.show {
						setTitle(R.string.permission_needed)
						message(R.string.location_permission_description)
						positiveButton(R.string.open_setting) {
							dismiss()
							val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
							val uri = Uri.fromParts("package", requireContext().packageName, null)
							intent.data = uri
							startActivity(intent)
						}
						negativeButton(R.string.close) { dismiss() }
					}
			} else {
				MaterialDialog(requireContext())
					.show {
						setTitle(R.string.permission_needed)
						message(R.string.location_permission_description)
						positiveButton(R.string.agree) {
							dismiss()
							permissionLauncher.launch(necessaryPermissions.toTypedArray())
						}
						negativeButton(R.string.close) { dismiss() }
					}
			}
		} else {
			turnOnLocation()
		}
	}

	private fun setMapCenter(location: Location? = null) {
		location?.also {
			target = LatLng(it.latitude, it.longitude)
		}

		val cameraUpdate = CameraUpdateFactory.newLatLngZoom(target, 17f)
		mGoogleMap?.animateCamera(cameraUpdate)
	}

	private fun loadMapFragment() {
		mapFragment = SupportMapFragment.newInstance()
		childFragmentManager.beginTransaction()
			.replace(R.id.fl_map, mapFragment)
			.commit()

		mapFragment.getMapAsync(mapReadyCallback)
	}

	@SuppressLint("MissingPermission")
	private fun turnOnLocation() {
		mGoogleMap?.also {
			it.isMyLocationEnabled = true
			it.uiSettings.isMyLocationButtonEnabled = false
		}
		locationProvider.startLocationUpdates()
	}
}