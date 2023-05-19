package eu.luminis.sample.eventsourced.snappable

import java.time.Instant
import java.util.*

class OrderBasketCommand

sealed class BasketEvent
data class BasketCreatedEvent(val dateCreated: Instant) : BasketEvent()
data class BasketItemAddedEvent(val productId: UUID, val amount: Int) : BasketEvent()
data class BasketItemRemovedEvent(val productId: UUID, val amount: Int) : BasketEvent()
data class BasketOrderedEvent(val dateOrdered: Instant) : BasketEvent()