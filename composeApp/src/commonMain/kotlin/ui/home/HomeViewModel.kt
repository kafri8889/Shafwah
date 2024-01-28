package ui.home

import base.ComposeViewModel
import currentTimeInMillis
import data.IceTeaVariant
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import data.model.IceTea
import repository.IceTeaRepository
import kotlin.random.Random

class HomeViewModel(
    private val iceTeaRepository: IceTeaRepository
): ComposeViewModel() {

    // {variant : count}
    private val _orders = MutableStateFlow(mapOf<IceTeaVariant, Int>())
    val orders: StateFlow<Map<IceTeaVariant, Int>> = _orders

    // True if order being processed (uploading to db), false otherwise
    private val _isOrdering = MutableStateFlow(false)
    val isOrdering: StateFlow<Boolean> = _isOrdering

    fun changeOrderCount(variant: IceTeaVariant, newCount: Int) {
        viewModelScope.launch {
            _orders.update {
                it.toMutableMap().apply {
                    if (containsKey(variant)) {
                        newCount.coerceAtLeast(0).let {
                            if (it <= 0) remove(variant) else set(variant, it)
                        }
                    }
                    else put(variant, newCount)
                }
            }
        }
    }

    fun placeOrder() {
        viewModelScope.launch {
            _isOrdering.emit(true)

            val iceTeas = arrayListOf<IceTea>()

            for ((k, v) in orders.value) {
                repeat(v) {
                    iceTeas.add(
                        IceTea(
                            id = Random.nextLong(), // Ignore, auto generate on db
                            variant = k,
                            createdAt = currentTimeInMillis(),
                            price = k.price
                        )
                    )
                }
            }

            try {
                iceTeaRepository.postIceTea(*iceTeas.toTypedArray()).let {
                    if (it.data == iceTeas.size) {
                        _orders.emit(emptyMap())
                        sendEvent(HomeUiEvent.OrderSuccess)
                    }
                    else sendEvent(HomeUiEvent.OrderFailed("Some data was not inserted: data = ${iceTeas.size}, inserted = ${it.data}"))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                sendEvent(HomeUiEvent.OrderFailed("Unknown error: ${e.message}"))
            } finally {
                _isOrdering.emit(false)
            }
        }
    }

}