import unittest
import uuid

from orders import PurchaseOrder, CreatePurchaseOrderCommand, LegoSet, ShippingAddress, ChangeShippingAddressCommand, \
    ShipPurchaseOrderCommand, PurchaseOrderCreatedEvent, PurchaseOrderShippedEvent


class TestPurchaseOrder(unittest.TestCase):

    def setUp(self) -> None:
        super().setUp()

    def test_create_purchase_order(self):
        purchase_order_created_event, new_uuid, shipping_address = self.__create_new_purchase_order()
        self.assertIsNotNone(purchase_order_created_event)
        self.assertEqual(new_uuid, purchase_order_created_event.identifier)

        purchase_order = PurchaseOrder(events=[purchase_order_created_event])
        self.assertIsNotNone(purchase_order)
        self.assertEqual(new_uuid, purchase_order.identifier)
        self.assertEqual(2, len(purchase_order.ordered_sets))

        self.assertEqual(shipping_address, purchase_order_created_event.shipping_address)

    def test_create_purchase_order__missing_lego_set(self):
        with self.assertRaises(expected_exception=ValueError):
            new_uuid = uuid.uuid4()
            shipping_address = ShippingAddress(street="street", number="4", city="city")
            PurchaseOrder.create(
                CreatePurchaseOrderCommand(identifier=new_uuid, ordered_sets=[], shipping_address=shipping_address)
            )

    def test_change_shipping_address(self):
        purchase_order_created_event, new_uuid, shipping_address = self.__create_new_purchase_order()
        purchase_order = PurchaseOrder(events=[purchase_order_created_event])

        changed_shipping_address = ShippingAddress(street="Movedstreet", number="4A", city="Amsterdam")
        command = ChangeShippingAddressCommand(identifier=new_uuid, shipping_address=changed_shipping_address)
        shipping_address_changed_event = purchase_order.change_shipping_address(command=command)

        self.assertIsNotNone(shipping_address_changed_event)
        self.assertEqual(new_uuid, shipping_address_changed_event.identifier)
        self.assertEqual(changed_shipping_address, shipping_address_changed_event.shipping_address)

    def test_change_shipping_address__already_shipped(self):
        new_uuid = uuid.uuid4()
        purchase_order_created_event = PurchaseOrderCreatedEvent(identifier=new_uuid,
                                                                 ordered_sets=[LegoSet("one")],
                                                                 shipping_address=ShippingAddress(street="street",
                                                                                                  number="3",
                                                                                                  city="Amsterdam"))
        order_shipped_event = PurchaseOrderShippedEvent(identifier=new_uuid,
                                                        shipping_address=purchase_order_created_event.shipping_address)
        purchase_order = PurchaseOrder(events=[
            purchase_order_created_event,
            order_shipped_event
        ])

        print(purchase_order.shipped)
        with self.assertRaises(expected_exception=ValueError):
            changed_shipping_address = ShippingAddress(street="Movedstreet", number="4A", city="Amsterdam")
            command = ChangeShippingAddressCommand(identifier=new_uuid, shipping_address=changed_shipping_address)
            purchase_order.change_shipping_address(command=command)

    def test_ship_purchase_order(self):
        new_uuid = uuid.uuid4()
        purchase_order_created_event = PurchaseOrderCreatedEvent(identifier=new_uuid,
                                                                 ordered_sets=[LegoSet("one")],
                                                                 shipping_address=ShippingAddress(street="street",
                                                                                                  number="3",
                                                                                                  city="Amsterdam"))
        purchase_order = PurchaseOrder(events=[
            purchase_order_created_event,
        ])

        purchase_order_shipped_event = purchase_order.ship(ShipPurchaseOrderCommand(identifier=new_uuid))
        self.assertIsNotNone(purchase_order_shipped_event)
        self.assertEqual(new_uuid, purchase_order_shipped_event.identifier)

    @staticmethod
    def __create_new_purchase_order():
        new_uuid = uuid.uuid4()
        shipping_address = ShippingAddress(street="Shippingstreet", number="4A", city="Amsterdam")
        purchase_order_created_event = PurchaseOrder.create(
            CreatePurchaseOrderCommand(identifier=new_uuid,
                                       ordered_sets=[LegoSet("set-1"), LegoSet("set-2")],
                                       shipping_address=shipping_address)
        )
        return purchase_order_created_event, new_uuid, shipping_address
