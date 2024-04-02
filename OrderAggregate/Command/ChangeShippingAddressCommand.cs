using OrderAggregate.Values;

namespace OrderAggregate.Command;

public record ChangeShippingAddressCommand(Address ShippingAddress);


