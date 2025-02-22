package ir.ubaar.employeetask.presentation.address

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import dagger.hilt.android.AndroidEntryPoint
import ir.ubaar.employeetask.R
import ir.ubaar.employeetask.core.BaseFragment
import ir.ubaar.employeetask.core.extensions.navTo
import ir.ubaar.employeetask.core.extensions.observe
import ir.ubaar.employeetask.core.extensions.reachEnd
import ir.ubaar.employeetask.data.models.AddressModel
import ir.ubaar.employeetask.databinding.FragmentAddressesBinding
import ir.ubaar.employeetask.domain.network.NetworkResult
import ir.ubaar.employeetask.presentation.FragmentResults

@AndroidEntryPoint
class AddressesFragment : BaseFragment<FragmentAddressesBinding>(
	resId = R.layout.fragment_addresses,
	titleResId = R.string.addresses,
	isShowBackButton = false
) {
	//ViewModel
	private val viewModel: AddressesViewModel by viewModels()

	//Address adapter
	private val adapter: AddressAdapter = AddressAdapter().apply {
		recyclerViewCallback = this@AddressesFragment
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		setFragmentResultListener(FragmentResults.Address.saved, ::initFragmentResultListener)

		//Set observers
		setupObservers()
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		//Config recyclerview
		binding.rvAddresses.layoutManager = LinearLayoutManager(requireContext())
		binding.rvAddresses.setHasFixedSize(true)
		binding.rvAddresses.adapter = adapter

		//Handle end of recyclerview scroll
		binding.rvAddresses.reachEnd { viewModel.loadNextChunk() }

		//Go to add new address
		binding.btnAddAddress.setOnClickListener {
			navTo(AddressesFragmentDirections.toAddAddress())
		}
	}

	//Get main view for toggle loading
	override fun getMainView(): ViewGroup {
		return binding.mainView
	}

	private fun initFragmentResultListener(requestKey: String, bundle: Bundle) {
		when (requestKey) {
			FragmentResults.Address.saved -> {
				val result = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
					bundle.getParcelable(FragmentResults.saved, AddressModel::class.java)
				} else {
					@Suppress("DEPRECATION")
					bundle.getParcelable(FragmentResults.saved)
				}
				logger.debug(result, "FResult")

				result?.also { mAddress ->
					adapter.mItems.add(0, mAddress)
					adapter.notifyItemInserted(0)
					binding.rvAddresses.scrollToPosition(0)
				}
			}
		}
	}

	private fun setupObservers() {
		viewModel.run {
			observe(isLoading(), ::initLoading)
			observe(getAddresses(), ::initAddresses)
		}
	}

	//Add items to adapter
	private fun addItems(addresses: List<AddressModel>) {
		val positionStart = adapter.mItems.size
		adapter.mItems.addAll(addresses)
		adapter.notifyItemRangeInserted(positionStart, addresses.size)
	}

	//Addresses observer callback
	private fun initAddresses(result: NetworkResult<List<AddressModel>>) {
		if (result is NetworkResult.Success) {
			logger.debug(result.data)
			addItems(result.data)
		} else if (result is NetworkResult.Error) {
			val errorMessage = if (result.data.message?.isNotEmpty() == true) {
				result.data.message
			} else {
				getString(R.string.response_error)
			}
			logger.error(result.data)
			MaterialDialog(requireContext())
				.noAutoDismiss()
				.show {
					//Disable outside click
					cancelable(false)

					//Set dialog message
					message(text = errorMessage)

					//Try again
					positiveButton(R.string.try_again) {
						viewModel.fetchData()
						dismiss()
					}

					//Close the dialog
					negativeButton(R.string.close) {
						dismiss()
						//Close app completely
						requireActivity().finishAffinity()
					}
				}
		}
	}
}