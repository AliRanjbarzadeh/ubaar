package ir.ubaar.employeetask.core.interfaces

import android.view.View
import androidx.core.widget.NestedScrollView

interface FragmentCallback {
	fun hideKeyboard() {}

	fun toggleBackButton(isShow: Boolean) {}

	fun scrollToTop(nestedScrollView: NestedScrollView) {
		nestedScrollView.fullScroll(View.FOCUS_UP)
		nestedScrollView.scrollTo(0, 0)
	}

	fun back() {}
}