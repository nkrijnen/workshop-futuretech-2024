package eu.luminis.sample.eventsourced.snappable

import java.time.Instant
import java.util.*
import kotlin.test.Test
import kotlin.test.assertFailsWith

class BasketAggregateTest {
    @Test
    fun `should not allow ordering empty basket`() {
        val history = listOf(
            BasketCreatedEvent(Instant.now()),
        )
        val order = BasketAggregate(BasketState().updateWith(history))

        assertFailsWith<IllegalStateException> {
            order.handle(OrderBasketCommand())
        }
    }

    @Test
    fun `should not allow ordering basket that became empty`() {
        val productId = UUID.randomUUID()
        val history = listOf(
            BasketCreatedEvent(Instant.now()),
            BasketItemAddedEvent(productId, 1),
            BasketItemRemovedEvent(productId, 1),
        )
        val order = BasketAggregate(BasketState().updateWith(history))

        assertFailsWith<IllegalStateException> {
            order.handle(OrderBasketCommand())
        }
    }
}
