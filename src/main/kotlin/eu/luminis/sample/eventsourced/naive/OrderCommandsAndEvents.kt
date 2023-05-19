package eu.luminis.sample.eventsourced.naive

import java.time.Instant
import java.util.*

data class CreateOrderCommand(val items: List<UUID>, val shippingAddress: Address)
data class ChangeShippingAddressCommand(val shippingAddress: Address)
data class ShipOrderCommand(val shippingProvider: String)

class AlreadyShippedException : IllegalStateException("Already shipped")

sealed class OrderEvent
data class OrderCreatedEvent(
    val dateCreated: Instant,
    val items: List<UUID>,
    val shippingAddress: Address
) : OrderEvent()

data class OrderShippedEvent(val dateShipped: Instant, val shippingProvider: String) : OrderEvent()
data class ShippingAddressChangedEvent(val dateChanged: Instant, val shippingAddress: Address) : OrderEvent()