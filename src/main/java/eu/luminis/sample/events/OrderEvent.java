package eu.luminis.sample.events;

public sealed interface OrderEvent permits OrderCreatedEvent, ShippingAddressChangedEvent, OrderShippedEvent {
}

