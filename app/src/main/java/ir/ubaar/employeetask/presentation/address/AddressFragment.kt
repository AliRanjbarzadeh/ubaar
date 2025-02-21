package ir.ubaar.employeetask.presentation.address

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
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

@AndroidEntryPoint
class AddressesFragment : BaseFragment<FragmentAddressesBinding>(
	resId = R.layout.fragment_addresses,
	titleResId = R.string.addresses,
	isShowBackButton = false
) {
	private val viewModel: AddressViewModel by viewModels()

	private val adapter: AddressAdapter = AddressAdapter().apply {
		recyclerViewCallback = this@AddressesFragment
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		setupObservers()
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		binding.rvAddresses.layoutManager = LinearLayoutManager(requireContext())
		binding.rvAddresses.setHasFixedSize(true)
		binding.rvAddresses.adapter = adapter

		binding.rvAddresses.reachEnd { viewModel.loadNextChunk() }

		binding.btnAddAddress.setOnClickListener {
			navTo(AddressesFragmentDirections.toAddAddress())
		}
	}

	override fun getMainView(): ViewGroup {
		return binding.mainView
	}

	private fun setupObservers() {
		viewModel.run {
			observe(isLoading(), ::initLoading)
			observe(getAddresses(), ::initAddresses)
		}
	}

	private fun addItems(addresses: List<AddressModel>) {
		val positionStart = adapter.mItems.size
		adapter.mItems.addAll(addresses)
		adapter.notifyItemRangeInserted(positionStart, addresses.size)
	}

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

					positiveButton(R.string.try_again) {
						viewModel.fetchData()
						dismiss()
					}
					negativeButton(R.string.close) {
						dismiss()
						requireActivity().finishAffinity()
					}
				}
		}
	}
}