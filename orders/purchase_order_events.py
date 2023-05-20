from uuid import UUID

from orders import LegoSet, ShippingAddress


class PurchaseOrderEvent:
    def __init__(self, identifier: UUID):
        self.identifier = identifier


class PurchaseOrderCreatedEvent(PurchaseOrderEvent):
    def __init__(self, identifier: UUID, ordered_sets: [LegoSet], shipping_address: ShippingAddress):
        super().__init__(identifier)
        self.ordered_sets = ordered_sets
        self.shipping_address = shipping_address


class ShippingAddressChangedEvent(PurchaseOrderEvent):
    def __init__(self, identifier: UUID, shipping_address: ShippingAddress):
        super().__init__(identifier)
        self.shipping_address = shipping_address


class PurchaseOrderShippedEvent(PurchaseOrderEvent):

    def __init__(self, identifier: UUID, shipping_address: ShippingAddress):
        super().__init__(identifier)
        self.shipping_address = shipping_address
