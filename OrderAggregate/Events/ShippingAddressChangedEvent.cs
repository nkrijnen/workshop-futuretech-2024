using OrderAggregate.Values;

namespace OrderAggregate.Events;

public record ShippingAddressChangedEvent(Address ShippingAddress) : IOrderEvent;