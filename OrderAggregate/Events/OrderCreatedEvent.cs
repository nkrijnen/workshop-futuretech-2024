using OrderAggregate.Values;

namespace OrderAggregate.Events;

public record OrderCreatedEvent(DateTime DateCreated, List<Guid> Items, Address ShippingAddress) : IOrderEvent;





