package eu.luminis.sample.events;

import eu.luminis.sample.values.Address;

public record ShippingAddressChangedEvent(
        Address shippingAddress
) implements OrderEvent {
}
