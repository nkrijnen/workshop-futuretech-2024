import type { Address, LegoSetNumber } from "./model.js";

export class CreateOrderCommand {
    constructor(
        readonly items: LegoSetNumber[],
        readonly shippingAddress: Address
    ) { }
}

export class ChangeShippingAddressCommand {
    constructor(
        readonly shippingAddress: Address
    ) { }
}
