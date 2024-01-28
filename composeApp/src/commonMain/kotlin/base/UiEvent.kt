package base

import androidx.compose.material3.SnackbarDuration
import uicomponent.bubble_bar.BubbleBarDuration

/**
 * UI event, digunakan ketika ingin menampilkan snackbar, toast, dll
 *
 * @author kafri8889
 */
open class UiEvent {

	object DismissCurrentSnackbar: UiEvent()

	open class ShowSnackbar(
		open val message: String,
		open val data: Any? = null,
		open val withDismissAction: Boolean = false,
		open val duration: SnackbarDuration = SnackbarDuration.Short,
		open val actionLabel: String? = null
	): UiEvent()

	open class ShowBubbleBar(
		open val message: String,
		open val data: Any? = null,
		open val withDismissAction: Boolean = false,
		open val duration: BubbleBarDuration = BubbleBarDuration.Short,
		open val actionLabel: String? = null
	): UiEvent()

}