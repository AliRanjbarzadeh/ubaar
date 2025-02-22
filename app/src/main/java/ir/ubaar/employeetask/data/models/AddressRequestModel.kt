package ir.ubaar.employeetask.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddressRequestModel(
	@SerializedName("region")
	var region: Int = 1,  // Region ID

	@SerializedName("first_name")
	var firstName: String,  // First name

	@SerializedName("last_name")
	var lastName: String,  // Last name

	@SerializedName("coordinate_mobile")
	var mobile: String,  // Mobile number

	@SerializedName("coordinate_phone_number")
	var phone: String,  // Phone number

	@SerializedName("address")
	var address: String,  // Address

	@SerializedName("lat")
	var lat: Double = 0.0,  // Latitude

	@SerializedName("lng")
	var lng: Double = 0.0,  // Longitude

	@SerializedName("gender")
	var gender: String,  // Gender
) : Parcelable