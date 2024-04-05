using OrderAggregate.Values;

namespace OrderAggregate.Command;

public record CreateOrderCommand(List<Guid> Items, Address ShippingAddress);
