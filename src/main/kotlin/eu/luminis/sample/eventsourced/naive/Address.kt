package eu.luminis.sample.eventsourced.naive

data class Address(
    val street: String,
    val postalCode: String,
    val city: String,
    val country: String,
)