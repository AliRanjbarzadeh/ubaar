package ir.ubaar.employeetask.presentation.address

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import ir.ubaar.employeetask.core.BaseAdapter
import ir.ubaar.employeetask.core.BaseHolder
import ir.ubaar.employeetask.core.interfaces.RecyclerViewCallback
import ir.ubaar.employeetask.data.models.AddressModel
import ir.ubaar.employeetask.databinding.AddressItemBinding

class AddressAdapter : BaseAdapter<AddressModel>() {
	lateinit var recyclerViewCallback: RecyclerViewCallback

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder<AddressModel> {
		val binding = AddressItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
		return object : BaseHolder<AddressModel>(binding) {
			override fun onBindUI(item: AddressModel, position: Int) {
				binding.item = item

				binding.txtMobile.isVisible = item.mobile.trim().isNotEmpty()

				binding.executePendingBindings()
			}
		}
	}
}