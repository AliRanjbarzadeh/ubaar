package ir.ubaar.employeetask.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddressModel(
	@SerializedName("id")
	val id: String,

	@SerializedName("address_id")
	val addressId: String,

	@SerializedName("region")
	val region: Region,

	@SerializedName("address")
	val address: String,

	@SerializedName("last_name")
	val lastName: String,

	@SerializedName("first_name")
	val firstName: String,

	@SerializedName("gender")
	val gender: String,

	@SerializedName("lat")
	val lat: Double,

	@SerializedName("lng")
	val lng: Double,

	@SerializedName("coordinate_mobile")
	val coordinateMobile: String,

	@SerializedName("coordinate_phone_number")
	val coordinatePhoneNumber: String,
) : Parcelable {

	val fullName: String
		get() = "$firstName $lastName"

	val mobile: String
		get() {
			if (coordinateMobile.startsWith("98")) {
				return coordinateMobile.replaceFirst("98", "0")
			}

			return coordinateMobile
		}

	@Parcelize
	data class Region(
		@SerializedName("id")
		val id: Int,

		@SerializedName("name")
		val name: String,

		@SerializedName("city_object")
		val city: City,

		@SerializedName("state_object")
		val state: State,
	) : Parcelable

	@Parcelize
	data class City(
		@SerializedName("city_id")
		val cityId: Int,

		@SerializedName("city_name")
		val cityName: String,
	) : Parcelable

	@Parcelize
	data class State(
		@SerializedName("state_id")
		val stateId: Int,

		@SerializedName("state_name")
		val stateName: String,
	) : Parcelable
}
