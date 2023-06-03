package eu.luminis.sample;

import eu.luminis.sample.commands.ChangeShippingAddressCommand;
import eu.luminis.sample.commands.CreateOrderCommand;
import eu.luminis.sample.commands.ShipOrderCommand;
import eu.luminis.sample.events.OrderCreatedEvent;
import eu.luminis.sample.events.OrderShippedEvent;
import eu.luminis.sample.exceptions.AlreadyShippedException;
import eu.luminis.sample.values.Address;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OrderAggregateTest {
    private final UUID millenniumFalconLegoSet = UUID.randomUUID();
    private final Address campusAddress = new Address("One Infinite Loop", "CA 95014", "Cupertino", "United States");
    private final Address operaHouseAddress = new Address("2 Macquarie Street", "NSW 2000", "Sydney", "Australia");

    @Test
    public void shouldCreateOrder() {
        var event = OrderAggregate.handle(
                new CreateOrderCommand(List.of(millenniumFalconLegoSet), campusAddress)
        );

        assertEquals(campusAddress, event.shippingAddress());
    }

    @Test
    public void shouldFailWhenMissingCreatedEvent() {
        assertThrows(IllegalStateException.class, () ->
                new OrderAggregate(List.of())
        );
    }

    @Test
    public void shouldAllowChangingShippingAddressWhenNotShipped() {
        var order = new OrderAggregate(List.of(
                new OrderCreatedEvent(Instant.now(), List.of(millenniumFalconLegoSet), campusAddress)
        ));

        var event = order.handle(
                new ChangeShippingAddressCommand(operaHouseAddress)
        );

        assertEquals(operaHouseAddress, event.shippingAddress());
    }

    @Test
    public void shouldNotAllowShippingOrderWhenAlreadyShipped() {
        var order = new OrderAggregate(List.of(
                new OrderCreatedEvent(Instant.now(), List.of(millenniumFalconLegoSet), campusAddress),
                new OrderShippedEvent(Instant.now(), "UPS")
        ));

        assertThrows(AlreadyShippedException.class, () ->
                order.handle(new ShipOrderCommand("UPS"))
        );
    }

    @Test
    public void shouldNotAllowChangingShippingOrderAfterBeingShipped() {
        var order = new OrderAggregate(List.of(
                new OrderCreatedEvent(Instant.now(), List.of(millenniumFalconLegoSet), campusAddress),
                new OrderShippedEvent(Instant.now(), "UPS")
        ));

        assertThrows(AlreadyShippedException.class, () ->
                order.handle(new ChangeShippingAddressCommand(operaHouseAddress))
        );
    }
}
