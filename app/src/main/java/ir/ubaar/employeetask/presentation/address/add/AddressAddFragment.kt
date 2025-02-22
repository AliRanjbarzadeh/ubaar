package ir.ubaar.employeetask.presentation.address.add

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import ir.ubaar.employeetask.R
import ir.ubaar.employeetask.core.BaseFragment
import ir.ubaar.employeetask.core.extensions.navTo
import ir.ubaar.employeetask.core.extensions.toEnglish
import ir.ubaar.employeetask.data.models.AddressRequestModel
import ir.ubaar.employeetask.databinding.FragmentAddressAddBinding

@AndroidEntryPoint
class AddressAddFragment : BaseFragment<FragmentAddressAddBinding>(
	resId = R.layout.fragment_address_add,
	titleResId = R.string.add_address,
	isShowBackButton = true
) {
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		binding.etFirstName.doAfterTextChanged { editable ->
			setEndIcon(binding.tilFirstName, editable.isNullOrEmpty())
		}

		binding.etLastName.doAfterTextChanged { editable ->
			setEndIcon(binding.tilLastName, editable.isNullOrEmpty())
		}

		binding.etMobile.doAfterTextChanged { editable ->
			setEndIcon(binding.tilMobile, editable.isNullOrEmpty())
		}

		binding.etPhone.doAfterTextChanged { editable ->
			setEndIcon(binding.tilPhone, editable.isNullOrEmpty())
		}

		binding.etAddress.doAfterTextChanged { editable ->
			setEndIcon(binding.tilAddress, editable.isNullOrEmpty())
		}

		binding.btnNext.setOnClickListener {
			//Create address request model
			val addressRequestModel = AddressRequestModel(
				firstName = binding.etFirstName.text.toString().trim().toEnglish(),
				lastName = binding.etLastName.text.toString().trim().toEnglish(),
				mobile = binding.etMobile.text.toString().trim().toEnglish(),
				phone = binding.etPhone.text.toString().trim().toEnglish(),
				address = binding.etAddress.text.toString().trim().toEnglish(),
				gender = if (binding.tbGender.checkedButtonId == R.id.btn_male) "male" else "female",
			)

			//Validation
			if (addressRequestModel.firstName.isEmpty()) {
				Toast.makeText(requireContext(), getString(R.string.enter_first_name), Toast.LENGTH_SHORT).show()
				return@setOnClickListener
			}

			if (addressRequestModel.lastName.isEmpty()) {
				Toast.makeText(requireContext(), getString(R.string.enter_last_name), Toast.LENGTH_SHORT).show()
				return@setOnClickListener
			}

			if (addressRequestModel.mobile.isEmpty()) {
				Toast.makeText(requireContext(), getString(R.string.enter_mobile), Toast.LENGTH_SHORT).show()
				return@setOnClickListener
			}

			if (addressRequestModel.phone.isEmpty()) {
				Toast.makeText(requireContext(), getString(R.string.enter_phone), Toast.LENGTH_SHORT).show()
				return@setOnClickListener
			}

			if (addressRequestModel.address.isEmpty()) {
				Toast.makeText(requireContext(), getString(R.string.enter_address), Toast.LENGTH_SHORT).show()
				return@setOnClickListener
			}
			//end of validation

			//Hide keyboard if open to prevent lag to go to next fragment
			hideKeyboard()

			//Go to map
			navTo(AddressAddFragmentDirections.toMap(addressRequestModel))
		}
	}

	//Listen on hide keyboard to clear focus of inputs
	override fun keyboardState(isShow: Boolean) {
		if (!isShow) {
			binding.etFirstName.clearFocus()
			binding.etLastName.clearFocus()
			binding.etMobile.clearFocus()
			binding.etPhone.clearFocus()
			binding.etAddress.clearFocus()
		}
	}

	//Change end icon of input
	private fun setEndIcon(textInputLayout: TextInputLayout, isError: Boolean) {
		if (isError) {
			textInputLayout.endIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.baseline_circle_24)
			textInputLayout.setEndIconTintList(ContextCompat.getColorStateList(requireContext(), R.color.md_theme_outline))
		} else {
			textInputLayout.endIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.baseline_check_circle_24)
			textInputLayout.setEndIconTintList(ContextCompat.getColorStateList(requireContext(), R.color.my_green))
		}
	}
}