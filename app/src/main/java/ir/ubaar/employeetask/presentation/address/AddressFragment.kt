package ir.ubaar.employeetask.presentation.address

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.afollestad.materialdialogs.MaterialDialog
import dagger.hilt.android.AndroidEntryPoint
import ir.ubaar.employeetask.R
import ir.ubaar.employeetask.core.BaseFragment
import ir.ubaar.employeetask.core.extensions.observe
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

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		setupObservers()
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
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

	private fun initAddresses(result: NetworkResult<List<AddressModel>>) {
		if (result is NetworkResult.Success) {
			logger.debug(result.data)
		} else if (result is NetworkResult.Error) {
			val errorMessage = if (result.data.message?.isNotEmpty() == true) {
				result.data.message
			} else {
				getString(R.string.response_error)
			}
			logger.error(result.data)
			MaterialDialog(requireContext()).show {
				message(text = errorMessage)
			}
		}
	}
}