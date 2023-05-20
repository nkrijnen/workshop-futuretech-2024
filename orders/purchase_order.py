from uuid import UUID

from orders import CreatePurchaseOrderCommand, ChangeShippingAddressCommand, ShipPurchaseOrderCommand
from orders.purchase_order_events import PurchaseOrderEvent, PurchaseOrderCreatedEvent, ShippingAddressChangedEvent, \
    PurchaseOrderShippedEvent


class PurchaseOrder:
    """
    pre-conditions:
    - Any order should have at least one order item
    - Need a shipping address, and the shipping address can be changed before the order is sent out

    """
    def __init__(self, events: [PurchaseOrderEvent]):
        self.__apply(events=events)

    def change_shipping_address(self, command: ChangeShippingAddressCommand) -> ShippingAddressChangedEvent:
        if not command.shipping_address:
            raise ValueError("The new shipping address is mandatory when requesting a change in shipping address.")

        if self.shipped:
            raise ValueError("Cannot change the shipping address if the order has already been shipped.")

        event = ShippingAddressChangedEvent(identifier=self.identifier, shipping_address=command.shipping_address)
        self.__apply_shipping_address_changed_event(event=event)
        return event

    def ship(self, command: ShipPurchaseOrderCommand):
        if self.shipped:
            raise ValueError("Cannot ship an already shipped order.")

        event = PurchaseOrderShippedEvent(identifier=self.identifier, shipping_address=self.shipping_address)
        self.__apply_purchase_order_shipped_event(event=event)
        return event

    def __apply(self, events: [PurchaseOrderEvent]):
        action_mapping = {
            "ShippingAddressChangedEvent": lambda obj: self.__apply_shipping_address_changed_event(obj),
            "PurchaseOrderShippedEvent": lambda obj: self.__apply_purchase_order_shipped_event(obj),
            "PurchaseOrderCreatedEvent": lambda obj: self.__apply_purchase_order_created_event(obj),
        }
        for event in events:
            class_name = event.__class__.__name__
            if class_name in action_mapping:
                action = action_mapping[class_name]
                action(event)
            else:
                raise ValueError("Unknown event to handle")

    def __apply_shipping_address_changed_event(self, event: ShippingAddressChangedEvent) -> None:
        self.shipping_address = event.shipping_address

    def __apply_purchase_order_shipped_event(self, event: PurchaseOrderShippedEvent) -> None:
        self.shipped = True

    def __apply_purchase_order_created_event(self, event: PurchaseOrderCreatedEvent) -> None:
        self.identifier = event.identifier
        self.ordered_sets = event.ordered_sets
        self.shipping_address = event.shipping_address
        self.shipped = False

    @staticmethod
    def create(command: CreatePurchaseOrderCommand) -> PurchaseOrderCreatedEvent:
        if not command.ordered_sets or len(command.ordered_sets) == 0:
            raise ValueError("There must be at least one LegoSet in the order to complete the order")
        return PurchaseOrderCreatedEvent(identifier=command.identifier,
                                         ordered_sets=command.ordered_sets,
                                         shipping_address=command.shipping_address)

