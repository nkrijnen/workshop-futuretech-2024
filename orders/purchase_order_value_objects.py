class LegoSet:

    def __init__(self, set_id: str) -> None:
        self.set_id = set_id


class ShippingAddress:

    def __init__(self, street: str, number: str, city: str) -> None:
        self.street = street
        self.number = number
        self.city = city
