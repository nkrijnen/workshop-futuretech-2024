package eu.luminis.sample.stateful

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class RentalAggregateTest {
    @Test
    fun `should send reminder`() {
        val rental = RentalAggregate(RentalRecord())

        val event = rental.handle(SendReminderCommand("OR3382", "harry@example.com"))

        assertEquals(1, rental.state.timesSent)
        assertEquals(ReminderSentEvent("OR3382", "harry@example.com"), event)
    }

    @Test
    fun `should send reminder 3 times`() {
        val rental = RentalAggregate(RentalRecord(timesSent = 2))

        val event = rental.handle(SendReminderCommand("OR3382", "harry@example.com"))

        assertEquals(3, rental.state.timesSent)
        assertEquals(ReminderSentEvent("OR3382", "harry@example.com"), event)
    }

    @Test
    fun `should not send reminder after 3 times`() {
        val rental = RentalAggregate(RentalRecord(timesSent = 3))

        assertFailsWith<IllegalStateException> {
            rental.handle(SendReminderCommand("OR3382", "harry@example.com"))
        }
    }
}
