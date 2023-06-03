package eu.luminis.sample;

import eu.luminis.sample.commands.ChangeShippingAddressCommand;
import eu.luminis.sample.commands.CreateOrderCommand;
import eu.luminis.sample.commands.ShipOrderCommand;
import eu.luminis.sample.events.OrderCreatedEvent;
import eu.luminis.sample.events.OrderEvent;
import eu.luminis.sample.events.OrderShippedEvent;
import eu.luminis.sample.events.ShippingAddressChangedEvent;
import eu.luminis.sample.exceptions.AlreadyShippedException;

import java.time.Instant;
import java.util.List;

public class OrderAggregate {

    private boolean hasBeenShipped;

    public static OrderCreatedEvent handle(CreateOrderCommand command) {
        return new OrderCreatedEvent(Instant.now(), command.items(), command.shippingAddress());
    }

    public OrderAggregate(List<OrderEvent> history) {
        if (!firstEventIsOrderCreated(history)) {
            throw new IllegalStateException("Missing order created event");
        }

        applyStateFrom(history);
    }

    public ShippingAddressChangedEvent handle(ChangeShippingAddressCommand command) {
        if (hasBeenShipped) throw new AlreadyShippedException();

        return new ShippingAddressChangedEvent(command.shippingAddress());
    }

    public OrderShippedEvent handle(ShipOrderCommand command) {
        if (hasBeenShipped) throw new AlreadyShippedException();

        return new OrderShippedEvent(Instant.now(), command.shippingProvider());
    }

    private void applyStateFrom(List<OrderEvent> events) {
        for (OrderEvent e : events) {
            switch (e) {
                case OrderCreatedEvent event -> {
                }
                case ShippingAddressChangedEvent event -> {
                }
                case OrderShippedEvent event -> hasBeenShipped = true;
            }
        }
    }

    private static boolean firstEventIsOrderCreated(List<OrderEvent> history) {
        return !history.isEmpty() && history.get(0) instanceof OrderCreatedEvent;
    }
}
