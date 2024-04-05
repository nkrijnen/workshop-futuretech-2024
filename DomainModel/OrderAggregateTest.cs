using OrderAggregate.Command;
using OrderAggregate.Events;
using OrderAggregate.Exceptions;
using OrderAggregate.Values;

namespace OrderAggregate;

[TestClass]
public class OrderAggregateTest
{
    private readonly Guid _millenniumFalconLegoSet = new Guid();
    private readonly Address _campusAddress = new Address("One Infinite Loop", "CA 95014", "Cupertino", "United States");
    private readonly Address _operaHouseAddress = new Address("2 Macquarie Street", "NSW 2000", "Sydney", "Australia");

    [TestMethod]
    public void ShouldCreateOrder()
    {
        var orderCreatedEvent = OrderAggregate.Handle(
            new CreateOrderCommand([_millenniumFalconLegoSet], _campusAddress)
        );

        Assert.AreEqual(_campusAddress, orderCreatedEvent.ShippingAddress);
    }

    [TestMethod]
    public void ShouldFailWhenMissingCreatedEvent()
    {
        Assert.ThrowsException<InvalidOperationException>(() => new OrderAggregate([]));
    }

    [TestMethod]
    public void ShouldAllowChangingShippingAddressWhenNotShipped()
    {
        var order = new OrderAggregate([new OrderCreatedEvent(DateTime.Now, [_millenniumFalconLegoSet], _campusAddress)]);

        var changeShippingEvent = order.Handle(new ChangeShippingAddressCommand(_operaHouseAddress));

        Assert.AreEqual(_operaHouseAddress, changeShippingEvent.ShippingAddress);
    }

    [TestMethod]
    public void ShouldNotAllowShippingOrderWhenAlreadyShipped()
    {
        var order = new OrderAggregate([new OrderCreatedEvent(DateTime.Now, [_millenniumFalconLegoSet], _campusAddress),
        new OrderShippedEvent(DateTime.Now, "UPS")]);

        Assert.ThrowsException<AlreadyShippedException>(() => order.Handle(new ShipOrderCommand("UPS")));
    }

    [TestMethod]
    public void ShouldNotAllowChangingShippingOrderAfterBeingShipped()
    {
        var order = new OrderAggregate([
                new OrderCreatedEvent(DateTime.Now, [_millenniumFalconLegoSet], _campusAddress),
                new OrderShippedEvent(DateTime.Now, "UPS")
        ]);

        Assert.ThrowsException<AlreadyShippedException>(() => order.Handle(new ChangeShippingAddressCommand(_operaHouseAddress)));
    }
}