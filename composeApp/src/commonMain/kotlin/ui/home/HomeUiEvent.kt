package ui.home

import base.UiEvent

object HomeUiEvent {

    data class OrderFailed(
        override val message: String,
        override val withDismissAction: Boolean = true
    ): UiEvent.ShowBubbleBar(message, withDismissAction = withDismissAction)

    data object OrderSuccess: UiEvent.ShowBubbleBar(
        message = "Order success",
        withDismissAction = true
    )

}