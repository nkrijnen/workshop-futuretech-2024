from uuid import UUID

from orders import LegoSet, ShippingAddress


class CreatePurchaseOrderCommand:

    def __init__(self, identifier: UUID, ordered_sets: [LegoSet], shipping_address: ShippingAddress):
        self.identifier = identifier
        self.ordered_sets = ordered_sets
        self.shipping_address = shipping_address


class ChangeShippingAddressCommand:

    def __init__(self, identifier: UUID, shipping_address: ShippingAddress) -> None:
        self.identifier = identifier
        self.shipping_address = shipping_address


class ShipPurchaseOrderCommand:

    def __init__(self, identifier: UUID) -> None:
        self.identifier = identifier
