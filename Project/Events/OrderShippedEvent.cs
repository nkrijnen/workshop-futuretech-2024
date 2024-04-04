namespace OrderAggregate.Events;

public record OrderShippedEvent(DateTime DateShipped, string ShippingProvider) : IOrderEvent;
