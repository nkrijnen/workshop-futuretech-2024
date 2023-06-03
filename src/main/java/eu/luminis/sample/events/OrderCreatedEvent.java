package eu.luminis.sample.events;

import eu.luminis.sample.values.Address;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record OrderCreatedEvent(
        Instant dateCreated,
        List<UUID> items,
        Address shippingAddress
) implements OrderEvent {
}
