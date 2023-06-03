package eu.luminis.sample.events;

import java.time.Instant;

public record OrderShippedEvent(
        Instant dateShipped,
        String shippingProvider
) implements OrderEvent {
}
