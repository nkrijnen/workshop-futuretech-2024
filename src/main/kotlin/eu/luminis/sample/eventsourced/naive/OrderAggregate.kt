package eu.luminis.sample.eventsourced.naive

import java.time.Instant

class OrderAggregate(private val history: List<OrderEvent>) {

    init {
        check(history.firstEventIsOrderCreated()) { "Missing order created event" }
    }

    companion object {
        @JvmStatic
        fun handle(command: CreateOrderCommand): OrderCreatedEvent {
            return OrderCreatedEvent(Instant.now(), command.items, command.shippingAddress)
        }
    }

    private val hasBeenShipped: Boolean by lazy { history.any { it is OrderShippedEvent } }

    fun handle(command: ChangeShippingAddressCommand): ShippingAddressChangedEvent {
        if (hasBeenShipped) throw AlreadyShippedException()

        return ShippingAddressChangedEvent(Instant.now(), command.shippingAddress)
    }

    fun handle(command: ShipOrderCommand): OrderShippedEvent {
        if (hasBeenShipped) throw AlreadyShippedException()

        return OrderShippedEvent(Instant.now(), command.shippingProvider)
    }

}

private fun List<OrderEvent>.firstEventIsOrderCreated() = isNotEmpty() && first() is OrderCreatedEvent