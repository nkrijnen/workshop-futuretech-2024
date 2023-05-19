package eu.luminis.sample.stateful

class RentalAggregate(private var rentalState: RentalRecord) {

    val state: RentalRecord
        get() = rentalState

    fun handle(command: SendReminderCommand): ReminderSentEvent {
        if (state.timesSent >= 3) throw IllegalStateException()

        rentalState = rentalState.incrementTimesSent()
        with(command) {
            return ReminderSentEvent(orderNumber, emailAddress)
        }
    }

}

data class RentalRecord(val timesSent: Int = 0) {
    fun incrementTimesSent() = copy(timesSent = timesSent + 1)
}

data class SendReminderCommand(val orderNumber: String, val emailAddress: String)

data class ReminderSentEvent(val orderNumber: String, val emailAddress: String)
