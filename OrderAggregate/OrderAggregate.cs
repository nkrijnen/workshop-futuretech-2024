using OrderAggregate.Command;
using OrderAggregate.Events;
using OrderAggregate.Exceptions;

namespace OrderAggregate;

public class OrderAggregate
{
    private bool _hasBeenShipped;

    public static OrderCreatedEvent Handle(CreateOrderCommand command)
    {
        return new OrderCreatedEvent(DateTime.Now, command.Items, command.ShippingAddress);
    }

    public OrderAggregate(List<IOrderEvent> history)
    {
        if (!FirstEventIsOrderCreated(history))
        {
            throw new InvalidOperationException("Missing order created event");
        }

        ApplyStateFrom(history);
    }

    public ShippingAddressChangedEvent Handle(ChangeShippingAddressCommand command)
    {
        if (_hasBeenShipped) throw new AlreadyShippedException();

        return new ShippingAddressChangedEvent(command.ShippingAddress);
    }

    public OrderShippedEvent Handle(ShipOrderCommand command)
    {
        if (_hasBeenShipped) throw new AlreadyShippedException();

        return new OrderShippedEvent(DateTime.Now, command.ShippingProvider);
    }

    private void ApplyStateFrom(List<IOrderEvent> events)
    {
        foreach (var e in events)
        {
            switch (e) 
            {
                case OrderCreatedEvent:
                {
                    break;
                }
                case ShippingAddressChangedEvent:
                {
                    break;
                }
                case OrderShippedEvent:
                {
                    _hasBeenShipped = true;
                    break;
                }
            }
        }
    }

    private static bool FirstEventIsOrderCreated(List<IOrderEvent> history)
    {
        return history.Any() && history[0] is OrderCreatedEvent;
    }
}
