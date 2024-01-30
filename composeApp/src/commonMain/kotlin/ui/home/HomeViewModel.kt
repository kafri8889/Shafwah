package ui.home

import DIMSUM_PER_PIECE
import base.ComposeViewModel
import currentTimeInMillis
import data.IceTeaVariant
import data.model.Dimsum
import data.model.IceTea
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import repository.DimsumRepository
import repository.IceTeaRepository
import kotlin.random.Random

class HomeViewModel(
    private val iceTeaRepository: IceTeaRepository,
    private val dimsumRepository: DimsumRepository
): ComposeViewModel() {

    // {variant : count}
    private val _iceTeaOrders = MutableStateFlow(mapOf<IceTeaVariant, Int>())
    val iceTeaOrders: StateFlow<Map<IceTeaVariant, Int>> = _iceTeaOrders

    // Dimsum amount
    private val _dimsumOrders = MutableStateFlow(0)

    /**
     * Dimsum amount
     */
    val dimsumOrders: StateFlow<Int> = _dimsumOrders

    // True if order being processed (uploading to db), false otherwise
    private val _isOrdering = MutableStateFlow(false)
    val isOrdering: StateFlow<Boolean> = _isOrdering

    /**
     * @param product [IceTeaVariant] or [Int]
     */
    fun changeIceTeaOrderCount(variant: IceTeaVariant, newCount: Int) {
        if (newCount < 0) return

        viewModelScope.launch {
            _iceTeaOrders.update { orderMap ->
                orderMap.toMutableMap().apply {
                    if (containsKey(variant)) {
                        if (newCount <= 0) remove(variant) else set(variant, newCount)
                    } else put(variant, newCount)
                }
            }
        }
    }

    fun changeDimsumOrderCount(newCount: Int) {
        if (newCount < 0) return

        viewModelScope.launch {
            _dimsumOrders.emit(newCount)
        }
    }

    fun clearOrders() {
        viewModelScope.launch {
            _dimsumOrders.emit(0)
            _iceTeaOrders.emit(emptyMap())
        }
    }

    fun placeOrder() {
        viewModelScope.launch {
            _isOrdering.emit(true)

            val iceTeas = arrayListOf<IceTea>()
            val dimsum = Dimsum(
                id = Random.nextLong(), // Ignore, auto generate on db,
                name = "Dimsum",
                unitPrice = DIMSUM_PER_PIECE,
                amount = dimsumOrders.value,
                price = DIMSUM_PER_PIECE * dimsumOrders.value,
                createdAt = currentTimeInMillis()
            )

            for ((variant, count) in iceTeaOrders.value) {
                repeat(count) {
                    iceTeas.add(
                        IceTea(
                            id = Random.nextLong(), // Ignore, auto generate on db
                            variant = variant,
                            createdAt = currentTimeInMillis(),
                            price = variant.price
                        )
                    )
                }
            }

            try {
                var insertedData = 0

                if (iceTeas.isNotEmpty()) {
                    iceTeaRepository.postIceTea(*iceTeas.toTypedArray()).let {
                        insertedData += it.data
                    }
                }

                if (dimsum.amount > 0) {
                    dimsumRepository.postDimsum(dimsum).let {
                        insertedData += it.data
                    }
                }

                if (insertedData == (iceTeas.size + dimsum.amount)) {
                    _iceTeaOrders.emit(emptyMap())
                    _dimsumOrders.emit(0)
                    sendEvent(HomeUiEvent.OrderSuccess)
                } else sendEvent(
                    HomeUiEvent.OrderFailed(
                        message = "Some order was not inserted: total order = ${iceTeas.size + dimsum.amount}, inserted = $insertedData"
                    )
                )
            } catch (e: Exception) {
                e.printStackTrace()
                sendEvent(HomeUiEvent.OrderFailed("Unknown error: ${e.message}"))
            } finally {
                _isOrdering.emit(false)
            }
        }
    }

}