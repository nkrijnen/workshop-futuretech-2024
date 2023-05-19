package eu.luminis.sample.eventsourced.snappable

import java.time.Instant
import java.util.*

class BasketAggregate(private val state: BasketState) {
    fun handle(command: OrderBasketCommand): BasketOrderedEvent {
        check(state.items.isNotEmpty() && state.items.all { it.value > 0 }) { "No items added to order" }

        return BasketOrderedEvent(Instant.now())
    }
}

data class BasketState(
    val dateCreated: Instant = Instant.now(),
    val items: Map<UUID, Int> = mapOf(),
    val dateOrdered: Instant? = null,
) {
    fun updateWith(history: List<BasketEvent>): BasketState = history.fold(this) { state, event ->
        when (event) {
            is BasketCreatedEvent -> state.copy(
                dateCreated = event.dateCreated
            )

            is BasketItemAddedEvent -> state.copy(
                items = state.items.adjustedWith(event.productId, event.amount)
            )

            is BasketItemRemovedEvent -> state.copy(
                items = state.items.adjustedWith(event.productId, -event.amount)
            )

            is BasketOrderedEvent -> state.copy(
                dateOrdered = event.dateOrdered
            )
        }
    }
}

private fun Map<UUID, Int>.adjustedWith(productId: UUID, amount: Int): Map<UUID, Int> {
    val currentAmount = this.getOrDefault(productId, 0)
    return this + (productId to (currentAmount + amount).coerceAtLeast(0))
}