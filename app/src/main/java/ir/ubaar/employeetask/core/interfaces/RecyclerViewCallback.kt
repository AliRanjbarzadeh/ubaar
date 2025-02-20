package ir.ubaar.employeetask.core.interfaces

import android.view.View

interface RecyclerViewCallback {
	fun <T> onItemClick(item: T, position: Int, view: View) {}
}