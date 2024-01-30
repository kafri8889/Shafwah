package ui.home

import CurrencyUtil
import DIMSUM_PER_PIECE
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import base.ComposableViewModel
import base.UiEvent
import data.IceTeaVariant
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import repository.DimsumRepository
import repository.IceTeaRepository
import uicomponent.*
import uicomponent.bubble_bar.*

@Composable
fun HomeScreen(
    iceTeaRepository: IceTeaRepository,
    dimsumRepository: DimsumRepository
) = ComposableViewModel(factory = { HomeViewModel(iceTeaRepository, dimsumRepository) }) { viewModel ->

    val iceTeaOrders by viewModel.iceTeaOrders.collectAsState()
    val dimsumOrders by viewModel.dimsumOrders.collectAsState()
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
                        IceTeaProductItem(
                            count = iceTeaOrders[variant] ?: 0,
                            variant = variant,
                            onValueChange = { newCount ->
                                viewModel.changeIceTeaOrderCount(variant, newCount)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }

                    item {
                        DimsumProductItem(
                            count = dimsumOrders,
                            price = DIMSUM_PER_PIECE,
                            imageRes = "dimsum.png",
                            onValueChange = viewModel::changeDimsumOrderCount
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
                        iceTeaOrders = iceTeaOrders,
                        dimsumOrders = dimsumOrders,
                        isOrdering = isOrdering,
                        onPlaceOrder = viewModel::placeOrder,
                        onClearAllOrders = viewModel::clearOrders,
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
    iceTeaOrders: Map<IceTeaVariant, Int>,
    dimsumOrders: Int,
    isOrdering: Boolean,
    modifier: Modifier = Modifier,
    onPlaceOrder: () -> Unit,
    onClearAllOrders: () -> Unit
) {

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "Clear all orders",
                style = MaterialTheme.typography.titleMedium
            )

            IconButton(onClick = onClearAllOrders) {
                Icon(
                    imageVector = Icons.Rounded.Clear,
                    contentDescription = null
                )
            }
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .weight(1f)
        ) {
            items(
                items = iceTeaOrders.keys.toList()
            ) { order ->
                IceTeaOrderItem(
                    count = iceTeaOrders[order]!!,
                    variant = order,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }

            if (dimsumOrders > 0) {
                item {
                    DimsumOrderItem(
                        count = dimsumOrders,
                        name = "Dimsum",
                        price = DIMSUM_PER_PIECE,
                        imageRes = "dimsum.png",
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }
        }

        PaymentSummary(
            iceTeaOrders = iceTeaOrders,
            dimsumOrders = dimsumOrders,
            modifier = Modifier
                .fillMaxWidth()
        )

        Button(
            enabled = (iceTeaOrders.isNotEmpty() || dimsumOrders > 0) and !isOrdering,
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
    iceTeaOrders: Map<IceTeaVariant, Int>,
    dimsumOrders: Int,
    modifier: Modifier = Modifier
) {

    val iceTeaTotal = remember(iceTeaOrders) {
        iceTeaOrders.map {
            it.key.price * it.value
        }.sum()
    }

    val dimsumTotal = remember(dimsumOrders) {
        dimsumOrders * DIMSUM_PER_PIECE
    }

    val total = remember(iceTeaTotal, dimsumTotal) {
        iceTeaTotal + dimsumTotal
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
                text = CurrencyUtil.toRupiah(iceTeaTotal.toInt()),
                style = MaterialTheme.typography.titleMedium
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "Dimsum",
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(Modifier.weight(1f))

            Text(
                text = CurrencyUtil.toRupiah(dimsumTotal.toInt()),
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
