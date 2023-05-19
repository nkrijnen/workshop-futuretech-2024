package eu.luminis.sample.eventsourced.naive

import java.time.Instant
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class OrderAggregateTest {
    private val millenniumFalconLegoSet = UUID.randomUUID()
    private val campusAddress = Address("One Infinite Loop", "CA 95014", "Cupertino", "United States")
    private val operaHouseAddress = Address("2 Macquarie Street", "NSW 2000", "Sydney", "Australia")

    @Test
    fun `should create order`() {
        val event = OrderAggregate.handle(
            CreateOrderCommand(listOf(millenniumFalconLegoSet), campusAddress)
        )

        assertEquals(campusAddress, event.shippingAddress)
    }

    @Test
    fun `should fail when missing created event`() {
        assertFailsWith<IllegalStateException> {
            OrderAggregate(emptyList())
        }
    }

    @Test
    fun `should allow changing shipping address as long as it's not shipped yet`() {
        val order = OrderAggregate(
            history = listOf(
                OrderCreatedEvent(Instant.now(), listOf(millenniumFalconLegoSet), campusAddress),
            )
        )

        val event = order.handle(
            ChangeShippingAddressCommand(operaHouseAddress)
        )

        assertEquals(operaHouseAddress, event.shippingAddress)
    }

    @Test
    fun `should not allow shipping order when already shipped`() {
        val order = OrderAggregate(
            history = listOf(
                OrderCreatedEvent(Instant.now(), listOf(millenniumFalconLegoSet), campusAddress),
                OrderShippedEvent(Instant.now(), "UPS"),
            )
        )

        assertFailsWith<AlreadyShippedException> {
            order.handle(ShipOrderCommand("UPS"))
        }
    }

    @Test
    fun `should not allow changing shipping address after being shipped`() {
        val order = OrderAggregate(
            history = listOf(
                OrderCreatedEvent(Instant.now(), listOf(millenniumFalconLegoSet), campusAddress),
                OrderShippedEvent(Instant.now(), "UPS"),
            )
        )

        assertFailsWith<AlreadyShippedException> {
            order.handle(ChangeShippingAddressCommand(operaHouseAddress))
        }
    }
}