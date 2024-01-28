package ui.home

import CurrencyUtil
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import base.ComposableViewModel
import base.UiEvent
import data.IceTeaVariant
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import repository.IceTeaRepository
import uicomponent.DashedDivider
import uicomponent.IceTeaVariantItem
import uicomponent.OrderItem
import uicomponent.bubble_bar.*

@Composable
fun HomeScreen(
    iceTeaRepository: IceTeaRepository
) = ComposableViewModel(factory = { HomeViewModel(iceTeaRepository) }) { viewModel ->

    val orders by viewModel.orders.collectAsState()
    val isOrdering by viewModel.isOrdering.collectAsState()

    val bubbleHostState = remember { BubbleBarHostState() }

    LaunchedEffect(viewModel) {
        viewModel.uiEvent.filterNotNull().collectLatest { event ->
            when (event) {
                is UiEvent.ShowBubbleBar -> {
                    // TODO: Bug snackbar/bubbleBar only appear once, jadi set value ke currentBubbleBarData langsung kalo mau munculin
                    bubbleHostState.currentBubbleBarData = object : BubbleBarData {
                        override val visuals: BubbleBarVisuals
                            get() = object : BubbleBarVisuals {
                                override val message: String
                                    get() = event.message
                                override val actionLabel: String?
                                    get() = event.actionLabel
                                override val withDismissAction: Boolean
                                    get() = event.withDismissAction
                                override val duration: BubbleBarDuration
                                    get() = event.duration
                            }

                        override fun performAction() {}
                        override fun dismiss() { bubbleHostState.currentBubbleBarData = null }
                    }
                }
            }
        }
    }

    BubbleBarHost(
        hostState = bubbleHostState,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Scaffold(
            containerColor = Color.LightGray.copy(alpha = 0.32f)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(7),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(0.8f)
                        .padding(16.dp)
                ) {
                    items(
                        items = IceTeaVariant.entries
                    ) { variant ->
                        IceTeaVariantItem(
                            count = orders[variant] ?: 0,
                            variant = variant,
                            onValueChange = { newCount ->
                                viewModel.changeOrderCount(variant, newCount)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                }

                Card(
                    shape = RoundedCornerShape(0),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 2.dp
                    ),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(0.2f)
                ) {
                    OrderContent(
                        orders = orders,
                        isOrdering = isOrdering,
                        onPlaceOrder = viewModel::placeOrder,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun OrderContent(
    orders: Map<IceTeaVariant, Int>,
    isOrdering: Boolean,
    modifier: Modifier = Modifier,
    onPlaceOrder: () -> Unit
) {

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .weight(1f)
        ) {
            items(
                items = orders.keys.toList()
            ) { order ->
                OrderItem(
                    count = orders[order]!!,
                    iceTeaVariant = order,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }

        PaymentSummary(
            orders = orders,
            modifier = Modifier
                .fillMaxWidth()
        )

        Button(
            enabled = orders.isNotEmpty() and !isOrdering,
            onClick = onPlaceOrder,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            if (isOrdering) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(24.dp)
                )
            } else Text("Place Order")
        }
    }
}

@Composable
private fun PaymentSummary(
    orders: Map<IceTeaVariant, Int>,
    modifier: Modifier = Modifier
) {

    val iceTea = remember(orders) {
        orders.map {
            it.key.price * it.value
        }.sum()
    }

    val total = remember(iceTea) {
        iceTea
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        Text(
            text = "Payment Summary",
            style = MaterialTheme.typography.titleMedium
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "Es teh",
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(Modifier.weight(1f))

            Text(
                text = CurrencyUtil.toRupiah(iceTea.toInt()),
                style = MaterialTheme.typography.titleMedium
            )
        }

        DashedDivider(
            thickness = 1.dp,
            modifier = Modifier
                .fillMaxWidth()
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "Total",
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(Modifier.weight(1f))

            Text(
                text = CurrencyUtil.toRupiah(total.toInt()),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}
